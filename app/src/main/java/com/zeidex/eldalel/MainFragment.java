package com.zeidex.eldalel;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.zeidex.eldalel.adapters.AccessoriesAdapter;
import com.zeidex.eldalel.adapters.HomeSliderAdapter;
import com.zeidex.eldalel.adapters.PhonesAdapter;
import com.zeidex.eldalel.adapters.ProductsCategory3Adapter;
import com.zeidex.eldalel.models.ProductsCategory;
import com.zeidex.eldalel.response.GetAddToCardResponse;
import com.zeidex.eldalel.response.GetAddToFavouriteResponse;
import com.zeidex.eldalel.response.GetHomeProducts;
import com.zeidex.eldalel.response.GetOffers;
import com.zeidex.eldalel.services.AddToCardApi;
import com.zeidex.eldalel.services.AddToFavouriteApi;
import com.zeidex.eldalel.services.HomeProducts;
import com.zeidex.eldalel.services.OffersAPI;
import com.zeidex.eldalel.utils.APIClient;
import com.zeidex.eldalel.utils.Animatoo;
import com.zeidex.eldalel.utils.ChangeLang;
import com.zeidex.eldalel.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zeidex.eldalel.utils.Constants.CART_NOT_EMPTY;
import static com.zeidex.eldalel.utils.Constants.SERVER_API_TEST;

public class MainFragment extends androidx.fragment.app.Fragment implements PhonesAdapter.PhonesOperation, AccessoriesAdapter.AccessoriesOperation, ProductsCategory3Adapter.ProductsCategory3Operation {
    @BindView(R.id.main_recycler_accessories)
    RecyclerView main_recycler_accessories;

    @BindView(R.id.main_recycler_phones)
    RecyclerView main_recycler_phones;

    @BindView(R.id.main_recycler_category3)
    RecyclerView main_recycler_category3;

    @BindView(R.id.fragment_main_searchview)
    SearchView fragment_main_searchview;

    @BindView(R.id.accessories_label)
    AppCompatTextView accessories_label;

    @BindView(R.id.phones_label)
    AppCompatTextView phones_label;

    @BindView(R.id.category3_label)
    AppCompatTextView category3_label;

    @BindView(R.id.fragment_main_basket_top_txt)
    AppCompatTextView fragment_main_basket_top_txt;

    @BindView(R.id.imageSlider)
    SliderView imageSlider;

    @OnClick(R.id.fragment_main_basket_top_constraint)
    public void goToBasket() {
        if (!PreferenceUtils.getUserLogin(getActivity()) && !PreferenceUtils.getCompanyLogin(getActivity())) {
            Toasty.error(getActivity(), getString(R.string.please_login_first), Toast.LENGTH_LONG).show();
            return;
        }
        MenuItem selectedItem;
        selectedItem = ((MainActivity) getActivity()).mBottomNav.getMenu().getItem(3);
        selectedItem.setChecked(true);
        ((MainActivity) getActivity()).selectFragment(selectedItem);


//            Fragment fragment = new BasketFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
////            ft.setCustomAnimations(R.anim.animate_slide_up_enter, R.anim.animate_slide_up_exit);
//            ft.replace(R.id.container_activity, fragment, "basket_fragment");
//            ft.addToBackStack(null);
//            ft.commit();
    }

    ProductsCategory3Adapter accessoriesAdapter;

    ProductsCategory3Adapter phonesAdapter;

    ProductsCategory3Adapter category3Adapter;

    HomeSliderAdapter homeSliderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        findViews();
        initializeRecycler();
    }

    public void findViews() {
        if (PreferenceUtils.getCompanyLogin(getActivity())) {
            token = PreferenceUtils.getCompanyToken(getActivity());
        } else if (PreferenceUtils.getUserLogin(getActivity())) {
            token = PreferenceUtils.getUserToken(getActivity());
        }

        if (token.equalsIgnoreCase("") || PreferenceUtils.getCountOfItemsBasket(getActivity()) <= 0) {
            fragment_main_basket_top_txt.setVisibility(View.GONE);
        } else {
            fragment_main_basket_top_txt.setVisibility(View.VISIBLE);
            fragment_main_basket_top_txt.setText("" + PreferenceUtils.getCountOfItemsBasket(getActivity()));
        }

        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.parseColor("#AAAAAA"));
        imageSlider.setIndicatorUnselectedColor(Color.parseColor("#FAFAFA"));
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :

        showDialog();
        onLoadPage();
    }

    public void initializeRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        main_recycler_accessories.setLayoutManager(layoutManager);
        main_recycler_accessories.setItemAnimator(new DefaultItemAnimator());

        main_recycler_phones.setLayoutManager(layoutManager2);
        main_recycler_phones.setItemAnimator(new DefaultItemAnimator());

        main_recycler_category3.setLayoutManager(layoutManager3);
        main_recycler_category3.setItemAnimator(new DefaultItemAnimator());
//
    }

    ArrayList<ProductsCategory> home_category1;
    ArrayList<ProductsCategory> home_category2;
    ArrayList<ProductsCategory> home_category3;
    ArrayList<String> categories_names;
    ArrayList<Integer> categories_ids;
    String token = "";

    public void onLoadPage() {
        home_category1 = new ArrayList<>();
        home_category2 = new ArrayList<>();
        home_category3 = new ArrayList<>();
        categories_names = new ArrayList<>();
        categories_ids = new ArrayList<>();
        reloadDialog.show();

        HomeProducts homeProducts = APIClient.getClient(SERVER_API_TEST).create(HomeProducts.class);
        Call<GetHomeProducts> getLoginResponseCall = homeProducts.getHomeProducts(token);
        getLoginResponseCall.enqueue(new Callback<GetHomeProducts>() {
            @Override
            public void onResponse(Call<GetHomeProducts> call, Response<GetHomeProducts> response) {
                GetHomeProducts getHomeProducts = response.body();

                int code = Integer.parseInt(getHomeProducts.getCode());
                if (code == 200) {

                    for (int i = 0, currentCategWithProducts = 0; i < getHomeProducts.getData().getCategories().size() && currentCategWithProducts < 3; i++) { //category loop
                        if (getHomeProducts.getData().getCategories().get(i).getProducts().size() == 0) {
                            continue;
                        }

                        Locale locale = ChangeLang.getLocale(getResources());
                        String loo = locale.getLanguage();
                        if (loo.equalsIgnoreCase("en")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category1.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category1.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }


                            }

                        } else if (loo.equalsIgnoreCase("ar")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName_ar());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category1.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category1.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }


                            }
                        }
                        break;
                    }

                    for (int i = 0; i < getHomeProducts.getData().getCategories().size(); i++) { //category loop
                        if (getHomeProducts.getData().getCategories().get(i).getProducts().size() == 0) {
                            continue;
                        }
                        int o = Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId());
                        if (categories_ids.get(0) == Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId())) {
                            continue;
                        }
                        Locale locale = ChangeLang.getLocale(getResources());
                        String loo = locale.getLanguage();
                        if (loo.equalsIgnoreCase("en")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category2.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category2.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }

                            }

                        } else if (loo.equalsIgnoreCase("ar")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName_ar());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category2.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category2.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }

                            }
                        }

                        break;
                    }

                    for (int i = 0; i < getHomeProducts.getData().getCategories().size(); i++) { //category loop
                        if (getHomeProducts.getData().getCategories().get(i).getProducts().size() == 0) {
                            continue;
                        }
                        int o = Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId());
                        if (categories_ids.get(1) == Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()) || categories_ids.get(0) == Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId())) {
                            continue;
                        }
                        Locale locale = ChangeLang.getLocale(getResources());
                        String loo = locale.getLanguage();
                        if (loo.equalsIgnoreCase("en")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category3.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category3.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }

                            }

                        } else if (loo.equalsIgnoreCase("ar")) {
                            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(i).getId()));
                            categories_names.add(getHomeProducts.getData().getCategories().get(i).getName_ar());

                            for (int j = 0; j < getHomeProducts.getData().getCategories().get(i).getProducts().size(); j++) { // product loop

                                String arr[] = getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar().split(" ", 2); // get first word
                                String firstWord = arr[0];

                                if (getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().size() == 0) {
                                    home_category3.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), "",
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                } else {
                                    home_category3.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPhotos().get(0).getFilename(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getName_ar(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getOld_price(),
                                            getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(i).getProducts().get(j).getAvailable_quantity()));
                                }

                            }
                        }

                        break;
                    }
                }
                category3Adapter = new ProductsCategory3Adapter(getActivity(), home_category3);
                category3Adapter.setProductsCategory3Operation(MainFragment.this);
                main_recycler_category3.setAdapter(category3Adapter);
                category3_label.setText(categories_names.get(2));

                phonesAdapter = new ProductsCategory3Adapter(getActivity(), home_category2);
                phonesAdapter.setProductsCategory3Operation(MainFragment.this);
                main_recycler_phones.setAdapter(phonesAdapter);
                phones_label.setText(categories_names.get(1));

                accessoriesAdapter = new ProductsCategory3Adapter(getActivity(), home_category1);
                accessoriesAdapter.setProductsCategory3Operation(MainFragment.this);
                main_recycler_accessories.setAdapter(accessoriesAdapter);
                accessories_label.setText(categories_names.get(0));
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetHomeProducts> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commitAllowingStateLoss();
            }
        });

        getSlidersData();
    }

    private List<ProductsCategory> getProducts(int categoryId, GetHomeProducts getHomeProducts) {
        ArrayList<ProductsCategory> home_category = new ArrayList<>();

        Locale locale = ChangeLang.getLocale(getResources());
        String loo = locale.getLanguage();
        if (loo.equalsIgnoreCase("en")) {
            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(categoryId).getId()));
            categories_names.add(getHomeProducts.getData().getCategories().get(categoryId).getName());

            for (int j = 0; j < getHomeProducts.getData().getCategories().get(categoryId).getProducts().size(); j++) { // product loop

                String arr[] = getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName().split(" ", 2); // get first word
                String firstWord = arr[0];

                if (getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPhotos().size() == 0) {
                    home_category.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getId(), "",
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getOld_price(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getAvailable_quantity()));
                } else {
                    home_category.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPhotos().get(0).getFilename(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getOld_price(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getAvailable_quantity()));
                }

            }

        } else if (loo.equalsIgnoreCase("ar")) {
            categories_ids.add(Integer.parseInt(getHomeProducts.getData().getCategories().get(categoryId).getId()));
            categories_names.add(getHomeProducts.getData().getCategories().get(categoryId).getName_ar());

            for (int j = 0; j < getHomeProducts.getData().getCategories().get(categoryId).getProducts().size(); j++) { // product loop

                String arr[] = getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName_ar().split(" ", 2); // get first word
                String firstWord = arr[0];

                if (getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPhotos().size() == 0) {
                    home_category.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getId(), "",
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName_ar(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getOld_price(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getAvailable_quantity()));
                } else {
                    home_category.add(new ProductsCategory(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getId(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPhotos().get(0).getFilename(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getDiscount(), firstWord, getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getName_ar(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getPrice(), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getOld_price(),
                            getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getFavorite(), String.valueOf(getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getCart()), getHomeProducts.getData().getCategories().get(categoryId).getProducts().get(j).getAvailable_quantity()));
                }

            }
        }
        return home_category;
    }


    private void getSlidersData() {
        OffersAPI offersAPI = APIClient.getClient(SERVER_API_TEST).create(OffersAPI.class);
        Call<GetOffers> getOffersCall = offersAPI.getOffers();
        getOffersCall.enqueue(new Callback<GetOffers>() {
            @Override
            public void onResponse(Call<GetOffers> call, Response<GetOffers> response) {
                GetOffers getOffers = response.body();
                int code = getOffers.getCode();
                if (code == 200) {
                    List<GetOffers.Offer> offers = response.body().getOffers();
                    homeSliderAdapter = new HomeSliderAdapter(getContext(), offers);
                    imageSlider.setSliderAdapter(homeSliderAdapter);
                    imageSlider.startAutoCycle();
                }
            }

            @Override
            public void onFailure(Call<GetOffers> call, Throwable t) {
                Toasty.error(getContext(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
            }
        });
    }

    int position_detail;

    @Override
    public void onClickPhone(int id, int pos) {
        position_detail = pos;
        startActivityForResult(new Intent(getActivity(), DetailItemActivity.class).putExtra("id", id).putExtra("similar_products", home_category2).putExtra("getLike", home_category2.get(pos).getLike()).putExtra("pos", pos), 111);
        Animatoo.animateSwipeLeft(getActivity());
    }

    @Override
    public void onCliickPhoneLike(int id) {
        reloadDialog.show();
        convertDaraToJson(id);
        AddToFavouriteApi addToFavouriteApi = APIClient.getClient(SERVER_API_TEST).create(AddToFavouriteApi.class);
        Call<GetAddToFavouriteResponse> getAddToFavouriteResponseCall = addToFavouriteApi.getAddToFavourite(post);
        getAddToFavouriteResponseCall.enqueue(new Callback<GetAddToFavouriteResponse>() {
            @Override
            public void onResponse(Call<GetAddToFavouriteResponse> call, Response<GetAddToFavouriteResponse> response) {
                GetAddToFavouriteResponse getAddToFavouriteResponse = response.body();
                if (Integer.parseInt(getAddToFavouriteResponse.getCode()) == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_favourites), Toast.LENGTH_LONG).show();
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToFavouriteResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    @Override
    public void onAddToPhoneCart(int id, int position) {
        prepareCartMap(id);
        AddToCardApi addToCardApi = APIClient.getClient(SERVER_API_TEST).create(AddToCardApi.class);
        Call<GetAddToCardResponse> getAddToCardResponseCall = addToCardApi.getAddToFavourite(post);
        getAddToCardResponseCall.enqueue(new Callback<GetAddToCardResponse>() {
            @Override
            public void onResponse(Call<GetAddToCardResponse> call, Response<GetAddToCardResponse> response) {
                GetAddToCardResponse getAddToCardResponse = response.body();
                if (getAddToCardResponse.getCode() == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_card), Toast.LENGTH_LONG).show();
                    phonesAdapter.getProductsCategoryList().get(position).setCart(String.valueOf(CART_NOT_EMPTY));
                    phonesAdapter.notifyItemChanged(position);
                    fragment_main_basket_top_txt.setText(getAddToCardResponse.getItemsCount());
                    fragment_main_basket_top_txt.setVisibility(View.VISIBLE);
                    PreferenceUtils.saveCountOfItemsBasket(getActivity(), Integer.parseInt(getAddToCardResponse.getItemsCount()));
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToCardResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    @Override
    public void onClickAcssesory(int id, int pos) {
        position_detail = pos;
        startActivityForResult(new Intent(getActivity(), DetailItemActivity.class).putExtra("id", id).putExtra("similar_products", home_category1).putExtra("getLike", home_category1.get(pos).getLike()).putExtra("pos", pos), 11111);
        Animatoo.animateSwipeLeft(getActivity());
    }


    Map<String, String> post;

    private void convertDaraToJson(int id) {
        post = new HashMap<>();
        if (PreferenceUtils.getUserLogin(getActivity())) {
            String token = PreferenceUtils.getUserToken(getActivity());
            post.put("product_id", String.valueOf(id));
            post.put("token", token);
        } else if (PreferenceUtils.getCompanyLogin(getActivity())) {
            String token = PreferenceUtils.getCompanyToken(getActivity());
            post.put("product_id", String.valueOf(id));
            post.put("token", token);
        }
    }

    @Override
    public void onCliickAccessoryLike(int id) {
        reloadDialog.show();
        convertDaraToJson(id);
        AddToFavouriteApi addToFavouriteApi = APIClient.getClient(SERVER_API_TEST).create(AddToFavouriteApi.class);
        Call<GetAddToFavouriteResponse> getAddToFavouriteResponseCall = addToFavouriteApi.getAddToFavourite(post);
        getAddToFavouriteResponseCall.enqueue(new Callback<GetAddToFavouriteResponse>() {
            @Override
            public void onResponse(Call<GetAddToFavouriteResponse> call, Response<GetAddToFavouriteResponse> response) {
                GetAddToFavouriteResponse getAddToFavouriteResponse = response.body();
                if (Integer.parseInt(getAddToFavouriteResponse.getCode()) == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_favourites), Toast.LENGTH_LONG).show();
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToFavouriteResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();

            }
        });
    }

    @Override
    public void onAddToAccessoryCart(int id, int position) {
        prepareCartMap(id);
        AddToCardApi addToCardApi = APIClient.getClient(SERVER_API_TEST).create(AddToCardApi.class);
        Call<GetAddToCardResponse> getAddToCardResponseCall = addToCardApi.getAddToFavourite(post);
        getAddToCardResponseCall.enqueue(new Callback<GetAddToCardResponse>() {
            @Override
            public void onResponse(Call<GetAddToCardResponse> call, Response<GetAddToCardResponse> response) {
                GetAddToCardResponse getAddToCardResponse = response.body();
                if (getAddToCardResponse.getCode() == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_card), Toast.LENGTH_LONG).show();
                    accessoriesAdapter.getProductsCategoryList().get(position).setCart(String.valueOf(CART_NOT_EMPTY));
                    accessoriesAdapter.notifyItemChanged(position);
                    fragment_main_basket_top_txt.setText(getAddToCardResponse.getItemsCount());
                    fragment_main_basket_top_txt.setVisibility(View.VISIBLE);
                    PreferenceUtils.saveCountOfItemsBasket(getActivity(), Integer.parseInt(getAddToCardResponse.getItemsCount()));
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToCardResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    Dialog reloadDialog;

    private void showDialog() {
        reloadDialog = new Dialog(getActivity());
        reloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reloadDialog.setContentView(R.layout.reload_layout);
        reloadDialog.setCancelable(false);
        reloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onClickProduct3(int id, int pos) {
        position_detail = pos;
        startActivityForResult(new Intent(getActivity(), DetailItemActivity.class).putExtra("id", id).putExtra("similar_products", home_category3).putExtra("getLike", home_category3.get(pos).getLike()).putExtra("pos", pos), 1111);
        Animatoo.animateSwipeLeft(getActivity());
    }

    @Override
    public void onCliickProductsCategory3Like(int id) {
        reloadDialog.show();
        convertDaraToJson(id);
        AddToFavouriteApi addToFavouriteApi = APIClient.getClient(SERVER_API_TEST).create(AddToFavouriteApi.class);
        Call<GetAddToFavouriteResponse> getAddToFavouriteResponseCall = addToFavouriteApi.getAddToFavourite(post);
        getAddToFavouriteResponseCall.enqueue(new Callback<GetAddToFavouriteResponse>() {
            @Override
            public void onResponse(Call<GetAddToFavouriteResponse> call, Response<GetAddToFavouriteResponse> response) {
                GetAddToFavouriteResponse getAddToFavouriteResponse = response.body();
                if (Integer.parseInt(getAddToFavouriteResponse.getCode()) == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_favourites), Toast.LENGTH_LONG).show();
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToFavouriteResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    @Override
    public void onAddToProductCategory3Cart(int id, int position) {
        prepareCartMap(id);
        AddToCardApi addToCardApi = APIClient.getClient(SERVER_API_TEST).create(AddToCardApi.class);
        Call<GetAddToCardResponse> getAddToCardResponseCall = addToCardApi.getAddToFavourite(post);
        getAddToCardResponseCall.enqueue(new Callback<GetAddToCardResponse>() {
            @Override
            public void onResponse(Call<GetAddToCardResponse> call, Response<GetAddToCardResponse> response) {
                GetAddToCardResponse getAddToCardResponse = response.body();
                if (getAddToCardResponse.getCode() == 200) {
                    Toasty.success(getActivity(), getString(R.string.add_to_card), Toast.LENGTH_LONG).show();
                    category3Adapter.getProductsCategoryList().get(position).setCart(String.valueOf(CART_NOT_EMPTY));
                    category3Adapter.notifyItemChanged(position);
                    fragment_main_basket_top_txt.setText(getAddToCardResponse.getItemsCount());
                    fragment_main_basket_top_txt.setVisibility(View.VISIBLE);
                    PreferenceUtils.saveCountOfItemsBasket(getActivity(), Integer.parseInt(getAddToCardResponse.getItemsCount()));
                }
                reloadDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetAddToCardResponse> call, Throwable t) {
                Toasty.error(getActivity(), getString(R.string.confirm_internet), Toast.LENGTH_LONG).show();
                reloadDialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data.getBooleanExtra("databack", false)) {
                ProductsCategory productsCategory = home_category2.get(position_detail);
                productsCategory.setLike("1");
                home_category2.set(position_detail, productsCategory);
                phonesAdapter.notifyItemChanged(position_detail);
            }
        } else if (requestCode == 11111) {
            if (data.getBooleanExtra("databack", false)) {
                ProductsCategory productsCategory = home_category1.get(position_detail);
                productsCategory.setLike("1");
                home_category1.set(position_detail, productsCategory);
                accessoriesAdapter.notifyItemChanged(position_detail);
            }

        } else if (requestCode == 1111) {
            if (data.getBooleanExtra("databack", false)) {
                ProductsCategory productsCategory = home_category3.get(position_detail);
                productsCategory.setLike("1");
                home_category3.set(position_detail, productsCategory);
                category3Adapter.notifyItemChanged(position_detail);
            }
        }
    }

    public void prepareCartMap(int id) {
        post = new HashMap<>();
        if (PreferenceUtils.getUserLogin(getActivity())) {
            String token = PreferenceUtils.getUserToken(getActivity());
            post.put("product_id", String.valueOf(id));
            post.put("token", token);
            post.put("quantity", "1");
        } else if (PreferenceUtils.getCompanyLogin(getActivity())) {
            String token = PreferenceUtils.getCompanyToken(getActivity());
            post.put("product_id", String.valueOf(id));
            post.put("token", token);
            post.put("quantity", "1");
        }
        reloadDialog.show();
    }
}

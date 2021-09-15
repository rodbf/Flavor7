package com.flavorplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.diet.DietFragment;
import com.flavorplus.recipes.explore.ExploreFragment;
import com.flavorplus.home.HomeFragment;
import com.flavorplus.login.LoginFragment;
import com.flavorplus.navbar.NavbarFragment;
import com.flavorplus.profile.ProfileFragment;
import com.flavorplus.recipes.bookdisplay.BookFragment;
import com.flavorplus.recipes.librarydisplay.LibraryFragment;
import com.flavorplus.recipes.recipedisplay.comments.CommentFragment;
import com.flavorplus.recipes.recipedisplay.view.RecipeDisplayFragment;
import com.flavorplus.recipes.week.WeekFragment;
import com.flavorplus.register.RegisterFragment;
import com.flavorplus.search.SearchFragment;
import com.flavorplus.search.SearchParameters;
import com.flavorplus.suggestions.SuggestionsFragment;
import com.flavorplus.support.landing.SupportFragment;
import com.flavorplus.support.models.Ticket;
import com.flavorplus.support.ticket.TicketFragment;
import com.flavorplus.util.NukeSSLCerts;
import com.flavorplus.util.volley.VolleySingleton;

import java.util.Objects;


public class MainActivity extends AppCompatActivity{

    private NavigationViewModel navigationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VolleySingleton.getInstance(this);
        NavigationSingleton.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        navigationViewModel.navigationController.observe(this, new Observer<NavigationViewModel.NavigationEvent>() {

            @Override
            public void onChanged(NavigationViewModel.NavigationEvent navigationEvent) {
                changeDisplayFragment(navigationEvent.getTargetScreen(), navigationEvent.getParameters());
            }
        });
        Fragment startingScreen;

        SharedPreferences userData = this.getSharedPreferences("userData", Context.MODE_PRIVATE);
        boolean isLoggedIn = !Objects.equals(userData.getString("jwt", ""), "");
        if(isLoggedIn)
            startingScreen = new HomeFragment();
        else
            startingScreen = new LoginFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_frame, new NavbarFragment());
        ft.replace(R.id.fragment_frame, startingScreen);
        ft.commit();
        NukeSSLCerts.nuke();

        changeDisplayFragment(NavigationViewModel.Screens.HOME);
    }

    private void changeDisplayFragment(int target){
        changeDisplayFragment(target, null);
    }

    private void changeDisplayFragment(int target, Object obj){
        Fragment fragment;
        Bundle bundle = new Bundle();

        switch(target){
            case NavigationViewModel.Screens.RECIPE_DISPLAY:
                fragment = new RecipeDisplayFragment();
                bundle.putInt("recipeId", navigationViewModel.getRecipeId());
                break;
            case NavigationViewModel.Screens.EXPLORE:
                fragment = new ExploreFragment();
                break;
            case NavigationViewModel.Screens.COMMENT_TEST:
                fragment = new CommentFragment();
                break;
            case NavigationViewModel.Screens.SEARCH:
                fragment = new SearchFragment((SearchParameters)obj);
                break;
            case NavigationViewModel.Screens.LOGIN:
                fragment = new LoginFragment();
                break;
            case NavigationViewModel.Screens.PROFILE:
                fragment = new ProfileFragment();
                break;
            case NavigationViewModel.Screens.REGISTER:
                fragment = new RegisterFragment();
                break;
            case NavigationViewModel.Screens.DIET:
                fragment = new DietFragment();
                break;
            case NavigationViewModel.Screens.LIBRARY:
                fragment = new LibraryFragment();
                break;
            case NavigationViewModel.Screens.WEEK:
                fragment = new WeekFragment();
                break;
            case NavigationViewModel.Screens.BOOKTEST:
                fragment = new BookFragment();
                bundle.putInt("bookId", navigationViewModel.getBookId());
                break;
            case NavigationViewModel.Screens.SUPPORT_LANDING:
                fragment = new SupportFragment();
                break;
            case NavigationViewModel.Screens.TICKET_DISPLAY:
                fragment = new TicketFragment();
                break;
            case NavigationViewModel.Screens.SUGGESTIONS:
                fragment = new SuggestionsFragment();
                break;
            default:
                fragment = new HomeFragment();
        }

        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        if(navigationViewModel.getAddtoBackStack()) {
            ft.addToBackStack(null);
        }
        else
            navigationViewModel.setAddtoBackStack(true);
        ft.commit();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}

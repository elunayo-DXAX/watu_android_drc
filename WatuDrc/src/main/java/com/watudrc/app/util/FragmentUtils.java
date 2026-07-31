package com.watudrc.app.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Utility class to safely perform fragment operations and prevent
 * IllegalStateException when fragments are not attached to activities.
 */
public class FragmentUtils {
    
    /**
     * Safely gets the activity from a fragment, checking if it's attached
     * @param fragment The fragment to get the activity from
     * @return The activity if the fragment is attached, null otherwise
     */
    public static FragmentActivity getSafeActivity(Fragment fragment) {
        if (fragment != null && fragment.isAdded() && fragment.getActivity() != null) {
            return fragment.getActivity();
        }
        return null;
    }
    
    /**
     * Safely gets the context from a fragment, checking if it's attached
     * @param fragment The fragment to get the context from
     * @return The context if the fragment is attached, null otherwise
     */
    public static android.content.Context getSafeContext(Fragment fragment) {
        if (fragment != null && fragment.isAdded() && fragment.getContext() != null) {
            return fragment.getContext();
        }
        return null;
    }
    
    /**
     * Safely performs a fragment transaction if the fragment is attached
     * @param fragment The fragment to perform the transaction from
     * @param transaction The transaction to perform
     * @return true if the transaction was performed, false otherwise
     */
    public static boolean safeFragmentTransaction(Fragment fragment, FragmentTransactionRunnable transaction) {
        FragmentActivity activity = getSafeActivity(fragment);
        if (activity != null) {
            try {
                transaction.run(activity.getSupportFragmentManager());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    /**
     * Safely pops the back stack if the fragment is attached
     * @param fragment The fragment to pop the back stack from
     * @return true if the operation was successful, false otherwise
     */
    public static boolean safePopBackStack(Fragment fragment) {
        FragmentActivity activity = getSafeActivity(fragment);
        if (activity != null) {
            try {
                activity.getSupportFragmentManager().popBackStack();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    /**
     * Interface for fragment transactions
     */
    public interface FragmentTransactionRunnable {
        void run(FragmentManager fragmentManager);
    }
} 
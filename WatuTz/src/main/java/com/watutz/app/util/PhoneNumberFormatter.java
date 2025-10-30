package com.watutz.app.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneNumberFormatter {

   public static void applyFormatting(final EditText editText) {
      editText.addTextChangedListener(new TextWatcher() {
         private boolean isFormatting;

         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action required here
         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No action required here
         }

         @Override
         public void afterTextChanged(Editable s) {
            if (isFormatting) return;

            isFormatting = true;

            // Remove all spaces from the input
            String unformatted = s.toString().replaceAll("\\s", "");

            // Format the input
            StringBuilder formatted = new StringBuilder();
            int length = unformatted.length();
            for (int i = 0; i < length; i++) {
               if (i == 4 || i == 7) { // Add space after the 4th and 7th characters
                  formatted.append(" ");
               }
               formatted.append(unformatted.charAt(i));
            }

            // Update the text with the formatted string
            editText.setText(formatted.toString());
            editText.setSelection(formatted.length()); // Place cursor at the end

            isFormatting = false;
         }
      });

      // Apply formatting immediately for pre-set text
      String currentText = editText.getText().toString();
      if (!currentText.isEmpty()) {
         editText.setText(currentText.replaceAll("\\s", "")); // Ensure spaces are removed
      }
   }


   // Method to format text for TextView
// Method to format text for TextView and replace international code with '0'
   public static String formatPhoneNumber(String phoneNumber) {
      // Remove any spaces or non-numeric characters from the input
      String unformatted = phoneNumber.replaceAll("[^\\d]", "");

      // Check if the phone number starts with an international code (e.g., +255)
      if (unformatted.length() > 0 && unformatted.charAt(0) == '2') { // Assuming '2' for East Africa (could be adjusted for other regions)
         // Replace international code with '0' if it's a valid international code
         unformatted = "0" + unformatted.substring(3); // Example: +255123456789 becomes 0123456789
      }

      // Format the number with spaces
      StringBuilder formatted = new StringBuilder();
      int length = unformatted.length();

      for (int i = 0; i < length; i++) {
         if (i == 3 || i == 6) { // Add space after the 3rd and 6th digits
            formatted.append(" ");
         }
         formatted.append(unformatted.charAt(i));
      }

      return formatted.toString();
   }
   // Method to apply the formatting to TextView
   public static void applyFormattingToTextView(TextView textView, String phoneNumber) {


      String formattedPhoneNumber = formatPhoneNumber(phoneNumber);
      textView.setText(formattedPhoneNumber);
   }
}


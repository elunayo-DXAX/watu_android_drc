package com.watudrc.app.view;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.watudrc.app.R;

@SuppressWarnings("unused")
public class PriceEditText extends EditText {

    private Locale locale = getResources().getConfiguration().locale;

    private Currency currency;

    private Locale defaultLocale = Locale.getDefault();

    private boolean defaultHintEnabled = true;
    private boolean allowNegativeValues = false;
    private long valueInLowestDenom = 0L;

    private CurrencyTextWatcher textWatcher;

    private String hintCache = null;
    /*
    PUBLIC METHODS
     */
    public PriceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        init();
        processAttributes(context, attrs);
        setLocale(locale);
    }

    private void init(){
        initCurrency();
        initCurrencyTextWatcher();
    }


    private void initCurrencyTextWatcher(){
        if(textWatcher != null){
            this.removeTextChangedListener(textWatcher);
        }
        textWatcher = new CurrencyTextWatcher(this, defaultLocale);
        this.addTextChangedListener(textWatcher);
    }

    private void initCurrency(){
        try {
            currency = Currency.getInstance(locale);
        } catch (IllegalArgumentException e) {
            currency = Currency.getInstance(defaultLocale);
        }
    }

    /**
     * Sets whether or or not the Default Hint (users local currency symbol) will be shown in the textbox when no value has yet been entered.
     * @param useDefaultHint - true to enable default hint, false to disable
     */
    public void setDefaultHintEnabled(boolean useDefaultHint) {
        this.defaultHintEnabled = useDefaultHint;
    }

    /**
     * Determine whether or not the default hint is currently enabled for this view.
     * @return true if the default hint is enabled, false if it is not.
     */
    public boolean getDefaultHintEnabled(){
        return this.defaultHintEnabled;
    }

    /**
     * Enable the user to input negative values
     */
    public void setAllowNegativeValues(boolean negativeValuesAllowed){
        allowNegativeValues = negativeValuesAllowed;
    }

    /**
     * Returns whether or not negative values have been allowed for this PriceEditText field
     */
    public boolean areNegativeValuesAllowed(){
        return allowNegativeValues;
    }

    /**
     * Retrieve the raw value that was input by the user in their currencies lowest denomination (e.g. pennies).
     *
     * IMPORTANT: Remember that the location of the decimal varies by currency/Locale. This method
     *  returns the raw given value, and does not account for locality of the user. It is up to the
     *  calling application to handle that level of conversion.
     *  For example, if the text of the field is $13.37, this method will return a long with a
     *  value of 1337, as penny is the lowest denomination for USD. It will be up to the calling
     *  application to know that it needs to handle this value as pennies and not some other denomination.
     *
     * @return The raw value that was input by the user, in the lowest denomination of that users
     *  locale.
     */
    public long getRawValue() {
        return valueInLowestDenom;
    }

    /**
     * Convenience method to retrieve the users Locale object. The same as calling
     * getResources().getConfiguration().locale
     *
     * @return the Locale object for the given users configuration
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Override the locale used by PriceEditText (which is the users device locale by default).
     * WARNING: If this method is used to set the locale to one not supported by ISO 3166,
     * formatting the text will throw an exception. Also keep in mind that calling this method
     * will set the hint based on the specified locale, which will override any previous hint value.
     * @param locale The locale to set the PriceEditText box to adhere to.
     */
    public void setLocale(Locale locale){
        this.locale = locale;
        init();
        updateHint();
    }

    public void setCurrency(Currency currency, Locale locale) {
        this.currency = currency;
        this.locale = locale;

        init();
        updateHint();
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;

        init();
        updateHint();
    }

    public Currency getCurrency() {
        return currency;
    }


    private void updateHint() {
        if(hintCache != null){
            setHint(hintCache);
        }
        else{
            if(defaultHintEnabled){
                setHint(getDefaultHintValue());
            }
        }
    }

    /**
     * Override the locale used by PriceEditText (which is the users device locale by default).
     * Defaults to Locale.US.
     * NOTE: Be absolutely sure that this value is supported by ISO 3166. See
     * Java.util.Locale.getISOCountries() for a list of currently supported ISO 3166 locales (note that this list
     * may not be identical on all devices)
     * @param locale The fallback locale to recover gracefully in the event of the PriceEditText's locale value failing.
     */
    public void setDefaultLocale(Locale locale){
        this.defaultLocale = locale;
        init();
    }

    public Locale getDefaultLocale(){
        return defaultLocale;
    }

    /**
     * Pass in a value to have it formatted using the same rules used during data entry. 
     * @param val A string which represents the value you'd like formatted. It is expected that this string will be in the same format returned by the getRawValue() method (i.e. a series of digits, such as 
     *            "1000" to represent "$10.00"). Note that formatCuurrency will take in ANY string, and will first strip any non-digit characters before working on that string. If the result of that processing
     *            reveals an empty string, or a string whose number of digits is greater than the max number of digits, an exception will be thrown.
     * @return A locale-formatted string of the passed in value, represented as currency.
     */
    public String formatCurrency(String val){
        return CurrencyTextFormatter.formatText(val, currency, locale, defaultLocale);
    }

    /**
     * Pass in a value to have it formatted using the same rules used during data entry.
     * @param rawVal A long which represents the value you'd like formatted. It is expected that this value will be in the same format returned by the getRawValue() method (i.e. a series of digits, such as
     *            "1000" to represent "$10.00").
     * @return A locale-formatted string of the passed in value, represented as currency.
     */
    public String formatCurrency(long rawVal){
        return CurrencyTextFormatter.formatText(String.valueOf(rawVal), currency, locale, defaultLocale);
    }

    protected void setValueInLowestDenom(Long mValueInLowestDenom) {
        this.valueInLowestDenom = mValueInLowestDenom;
    }


    /*
    PRIVATE HELPER METHODS
     */

    private void processAttributes(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PriceEditText);

        boolean defaultHintAttrVal = array.getBoolean(R.styleable.PriceEditText_enable_default_hint, true);
        configureHint(defaultHintAttrVal);

        this.setAllowNegativeValues(array.getBoolean(R.styleable.PriceEditText_allow_negative_values, false));

        array.recycle();
    }

    private void configureHint(boolean defaultHintAttrVal){

        if(hintAlreadySet()){
            this.setDefaultHintEnabled(false);
            this.hintCache = getHint().toString();
            return;
        }
        else{
            this.setDefaultHintEnabled(defaultHintAttrVal);
        }

        if(getDefaultHintEnabled()) {
            this.setHint(getDefaultHintValue());
        }
        else{
            Log.i(this.getClass().getSimpleName(), "configureHint: Default Hint disabled; ignoring request.");
        }
    }

    private boolean hintAlreadySet(){
        return (this.getHint() != null && !this.getHint().equals(""));
    }

    private String getDefaultHintValue() {
        return currency.getSymbol();
    }
    
    class CurrencyTextWatcher implements TextWatcher {

        private PriceEditText editText;
        private Locale defaultLocale;

        private boolean ignoreIteration;
        private String lastGoodInput;

        double CURRENCY_DECIMAL_DIVISOR;
        final int CURSOR_SPACING_COMPENSATION = 2;

        //Setting a max length because after this length, java represents doubles in scientific notation which breaks the formatter
        final int MAX_RAW_INPUT_LENGTH = 15;


        /**
         * A specialized TextWatcher designed specifically for converting EditText values to a pretty-print string currency value.
         * @param textBox The EditText box to which this TextWatcher is being applied.
         *                Used for replacing user-entered text with formatted text as well as handling cursor position for inputting monetary values
         */
        public CurrencyTextWatcher(PriceEditText textBox){
            this(textBox, Locale.UK);
        }

        /**
         * A specialized TextWatcher designed specifically for converting EditText values to a pretty-print string currency value.
         * @param textBox The EditText box to which this TextWatcher is being applied.
         *                Used for replacing user-entered text with formatted text as well as handling cursor position for inputting monetary values
         * @param defaultLocale optional locale to default to in the event that the provided PriceEditText locale fails due to being unsupported
         */
        public CurrencyTextWatcher(PriceEditText textBox, Locale defaultLocale){
            editText = textBox;
            lastGoodInput = "";
            ignoreIteration = false;
            this.defaultLocale = defaultLocale;

            //Different countries use different fractional values for denominations (0.999 <x> vs. 0.99 cents), therefore this must be defined at runtime
            try{
                CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, Currency.getInstance(editText.getLocale()).getDefaultFractionDigits());
            }
            catch(IllegalArgumentException e){
                Log.e("CurrencyTextWatcher", "Unsupported locale provided, defaulting to Locale.UK. Error: " + e.getMessage());
                CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, Currency.getInstance(defaultLocale).getDefaultFractionDigits());
            }
        }

        /**
         * After each letter is typed, this method will take in the current text, process it, and take the resulting
         * formatted string and place it back in the EditText box the TextWatcher is applied to
         * @param editable text to be transformed
         */
        @Override
        public void afterTextChanged(Editable editable) {
            //Use the ignoreIteration flag to stop our edits to the text field from triggering an endlessly recursive call to afterTextChanged
            if(!ignoreIteration){
                ignoreIteration = true;
                //Start by converting the editable to something easier to work with, then remove all non-digit characters
                String newText = editable.toString();
                String textToDisplay;

                newText = (editText.areNegativeValuesAllowed()) ? newText.replaceAll("[^0-9/-]", "") : newText.replaceAll("[^0-9]", "");
                if(!newText.equals("") && newText.length() < MAX_RAW_INPUT_LENGTH && !newText.equals("-")){
                    //Store a copy of the raw input to be retrieved later by getRawValue
                    editText.setValueInLowestDenom(Long.valueOf(newText));
                }
                try{
                    textToDisplay = CurrencyTextFormatter.formatText(newText, editText.getCurrency(), editText.getLocale(), defaultLocale);
                }
                catch(IllegalArgumentException exception){
                    textToDisplay = lastGoodInput;
                }

                editText.setText(textToDisplay);
                //Store the last known good input so if there are any issues with new input later, we can fall back gracefully.
                lastGoodInput = textToDisplay;

                //locate the position to move the cursor to. The CURSOR_SPACING_COMPENSATION constant is to account for locales where the Euro is displayed as " €" (2 characters).
                //A more robust cursor strategy will be implemented at a later date.
                int cursorPosition = editText.getText().length();
                if(textToDisplay.length() > 0 && Character.isDigit(textToDisplay.charAt(0))) cursorPosition -= CURSOR_SPACING_COMPENSATION;

                //Move the cursor to the end of the numerical value to enter the next number in a right-to-left fashion, like you would on a calculator.
                editText.setSelection(cursorPosition);

            }
            else{
                ignoreIteration = false;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
    }
    
    public final static class CurrencyTextFormatter {

        //Setting a max length because after this length, java represents doubles in scientific notation which breaks the formatter
        static final int MAX_RAW_INPUT_LENGTH = 15;

        private CurrencyTextFormatter(){}

        public static String formatText(String val, Currency currency, Locale locale){
            return formatText(val, currency, locale, Locale.UK);
        }

        public static String formatText(String val, Currency currency, Locale locale, Locale defaultLocale){
            //special case for the start of a negative number
            if(val.equals("-")) return val;

            double CURRENCY_DECIMAL_DIVISOR;
            DecimalFormat currencyFormatter = null;
            try{
                CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, currency.getDefaultFractionDigits());
                currencyFormatter = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
            }
            catch(IllegalArgumentException e){
                CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, Currency.getInstance(defaultLocale).getDefaultFractionDigits());
                currencyFormatter = (DecimalFormat) DecimalFormat.getCurrencyInstance(defaultLocale);
            }

            //if there's nothing left, that means we were handed an empty string. Also, cap the raw input so the formatter doesn't break.
            if(!val.equals("") && val.length() < MAX_RAW_INPUT_LENGTH && !val.equals("-")) {
                //Convert the string into a double, which will later be passed into the currency formatter
                double newTextValue = Double.valueOf(val);

                /** Despite having a formatter, we actually need to place the decimal ourselves.
                 * IMPORTANT: This double division does have a small potential to introduce rounding errors (though the likelihood is very small for two digits)
                 * Therefore, do not attempt to pull the numerical value out of the String text of this object. Instead, call getRawValue to retrieve
                 * the actual number input by the user. See PriceEditText.getRawValue() for more information.
                 */
                newTextValue = newTextValue / CURRENCY_DECIMAL_DIVISOR;
                val = currencyFormatter.format(newTextValue);
            }
            else {
                throw new IllegalArgumentException("Invalid amount of digits found (either zero or too many) in argument val");
            }
            return val;
        }

    }
}

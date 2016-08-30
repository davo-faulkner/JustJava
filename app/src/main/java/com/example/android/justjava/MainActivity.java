package com.example.android.justjava; /**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p/>
 * package com.example.android.justjava;
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.R;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    boolean hasWhippedCream = false;
    boolean hasChocolate = false;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method increments the quantity when the "+" button is clicked.
     */
    public void increment(View view) {
        if (quantity > 99) {
            // Show an error message as a toast
            Toast.makeText(this, R.string.toast_more_than_100_cups, Toast.LENGTH_SHORT).show();
            // Exit method early because user is attempting to order too many cups of coffee
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method decrements the quantity when the "-" button is clicked.
     */
    public void decrement(View view) {
        if (quantity < 2) {
            // Show an error message as a toast
            Toast.makeText(this, R.string.toast_less_than_1_cup, Toast.LENGTH_SHORT).show();
            // Exit method early because user is attempting to order too few cups of coffee
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        hasChocolate = chocolateCheckBox.isChecked();
        EditText nameEditText = (EditText) findViewById(R.id.name_edittext);
        name = nameEditText.getText().toString();
        int price = calculatePrice();
        String priceMessage = createOrderSummary(price);
        emailMessage(priceMessage);
    }

    /**
     * Calculates the price of the order.
     * @return total price
     */
    private int calculatePrice() {
        int pricePerCup = 5;
        // Add $1 if user wants whipped cream
        if (hasWhippedCream) {
            pricePerCup = pricePerCup + 1;
        }
        // Add $2 if user wants chocolate
        if (hasChocolate) {
            pricePerCup = pricePerCup + 2;
        }
        int price = quantity * pricePerCup;
        return price;
    }

    /**
     * Creates an order summary.
     * @param price total price of the order
     * @return order summary String
     */
    private String createOrderSummary (int price) {
        String priceMessage =  getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.add_whipped_cream) + " " + hasWhippedCream +
                "\n" + getString(R.string.add_chocolate) + " " + hasChocolate +
                "\n" + getString(R.string.quantity) + ": " + quantity +
                "\n" + getString(R.string.total)+ ": " + price +
                "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * This method emails the given text.
     */
    private void emailMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.just_java_order_for) + " " + name);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Shop[] shops = new Shop[6]; // initialization of an array of class shops
        shops[0] = new Shop("KVYVKamppi", "Helsinki", "Korkeankatu 43, 00100 Helsinki", 432584568);
        shops[1] = new Shop("KVYVRuoholahti", "Helsinki", "Ruoholahdenkatu 23, 00180 Helsinki", 432584568);
        shops[2] = new Shop("KVYVSuurpelto", "Espoo", "Klariksentie 83, 02250 Espoo", 353768720);
        shops[3] = new Shop("KVYVMatinkylä", "Espoo", "Piispansilta 17, 02230 Espoo", 353768720);
        shops[4] = new Shop("KVYVTikkurila", "Vantaa", "Heidehofintie 5, 01301 Vantaa;", 293893854);
        shops[5] = new Shop("KVYVHaukila", "Vantaa", "Kannuskuja 3, 01200 Vantaa", 293893854);

        Product[] products = new Product[10]; // initialization of an array of class products
        products[0] = new Product("Onion", 1.83, 100);
        products[1] = new Product("Tomato", 4.97, 100);
        products[2] = new Product("Apple", 2.13, 100);
        products[3] = new Product("Orange", 2.76, 100);
        products[4] = new Product("Grapes", 2.88, 100);
        products[5] = new Product("Strawberry", 5.26, 100);
        products[6] = new Product("Cucumber", 1.47, 100);
        products[7] = new Product("Blueberry", 7.59, 100);
        products[8] = new Product("Watermelon", 3.89, 100);
        products[9] = new Product("Honeymelon", 3.78, 100);

        AddToBasket[] items = new AddToBasket[products.length]; // initialization of an array of class AddToBasket, with the length of the amount of products

        String municipalityName; // initialization of the string municipalityName
        String shopName; // initialization of the string shopName
        String productName; // initialization of the string productName
        int addToBasketIndex = 0; // initialization of the integer addToBasketIndex

        /*any exception is caught and reported to the user. The scanner to read user input is released.*/
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Welcome to the Grocery Shopping Simulation!\nPress Enter to Continue.");
            scanner.nextLine();
            System.out.println();

            /*
              The municipality name is obtained as the input and is verified against the municipality names in the array shops by the method findShop
              If the municipality name exists, then the user chooses a shop from the shops in their municipality.
              If the municipality does not exist, then this is reported to the user.
              The shop name chosen by the user is obtained as the input and is verified against the shop names in the array shops by the method chooseShop
              If the shop exists, then the user continues to search for a product.
              If the shop does not exist, then this is reported to the user.
             */
            while (true) {
                System.out.print("Enter municipality: ");
                municipalityName = scanner.next(); // stores input of user in String municipalityName
                int y = findShop(municipalityName, shops); // stores the value of the function findShop, takes the parameters of the municipalityName given by the user
                if (y != 0) break;
            }

            while (true) {
                System.out.print("Choose Store (enter shop name): ");
                shopName = scanner.next();
                int y = chooseShop(municipalityName, shopName, shops);
                if (y != 0) break;
            }

            while (true) {
                /*
                 * The product is obtained as the input and verified against the item names in the class products by the method findProduct
                 * If the product exists then the user is asked if they would like to add it to their shopping cart.
                 * If the product does not exist then this is reported to the user.
                 */
                while (true) {
                    while (true) {
                        System.out.print("Search Product: ");
                        productName = scanner.next();
                        productName = productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase();
                        int y = findProduct(productName, products);
                        if (y != 0) break;
                    }
                    System.out.print("Add to cart? (yes or no): ");
                    int z = shopping(scanner.next());
                    while (z == 2) {
                        System.out.print("Enter either yes or no: ");
                        z = shopping(scanner.next());
                    }
                    if (z == 1) break;
                }
            /*
               The quantity is obtained as input and verified against the quantity available in the stock.
               If the stock exists for the requested product, then it is added to the cart.
               If the same product is ordered twice, then the quantity and cost is updated to the existing product in the cart
               If the stock does not exist for the requested product, then it is reported back to the user on non-availability.
             */
                System.out.print("How many kilograms? (give answer with units): ");
                double numKilos = scanner.nextDouble();
                double maxAmount = productBalance(productName, products);
                while (numKilos < 0) {
                    System.out.print("Enter number of kilograms (number must be greater than 0): ");
                    numKilos = scanner.nextDouble();
                }

                if (maxAmount != 0) {
                    while (numKilos > maxAmount) {
                        System.out.print("The maximum amount of kilograms you can buy is " + maxAmount + ".\nEnter a number less than " + maxAmount + ": ");
                        numKilos = scanner.nextDouble();
                    }
                    for (Product x : products) {
                        if (x.itemName.equals(productName)) {
                            x.numKilos = maxAmount - numKilos;
                        }
                    }

                    double cost = cost(numKilos, productName, products);
                    boolean productExists = false;
                    for (AddToBasket x : items) {
                        if ((x != null) && x.productName.equals(productName)) {
                            x.price += cost;
                            x.numKilos += numKilos;
                            productExists = true;
                            System.out.println("Added to cart.");
                            break;
                        }
                    }

                    if (!productExists) {
                        items[addToBasketIndex] = new AddToBasket(productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase(), numKilos, cost);
                        System.out.println("Added to cart.");
                        addToBasketIndex++;
                    }
                } else {
                    System.out.println("Available quantity of product is already ordered. No more can be ordered.");
                }

                System.out.print("Would you like to continue shopping? (yes or no): ");
                String continueShopping = scanner.next();
                int q = shopping(continueShopping);
                while (q == 2) {
                    System.out.print("Enter either yes or no: ");
                    q = shopping(scanner.next());
                }
                if (q == 0) break; // if the input to continue shopping is "No", then the loop will break.
            }
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("Bill");
            System.out.println();
            System.out.println("Product\t\t\t\t\tQuantity\t\t\t\tCost\n");
            /*traverses the array items until the addToBasketIndex, and prints them -- prints the products bought by the user*/
            for (int i = 0; i < addToBasketIndex; i++)
                System.out.println(items[i]);
            System.out.println();
            System.out.print("Total:\t\t\t\t\t\t\t\t\t\t\t");
            double totalPrice = 0;
            /*traverses the array items until the addToBasketIndex, and adds all the prices -- prints the total amount spent by the user*/
            for (int i = 0; i < addToBasketIndex; i++) {
                totalPrice += items[i].price;
            }
            System.out.print(String.format("%.2f", totalPrice) + "€\n\n");
            System.out.println("Thank you for shopping at " + shopName.substring(0, 5).toUpperCase() + shopName.substring(5).toLowerCase() + "!");
        } catch (Exception e) {
            System.out.println("\nException: " + e);
            System.exit(1);
        }
    }

    /**
     * this method takes in the parameters of the String municipalityName (user input) and the array shops
     * if there is a match in the municipalityName given by the user and the municipality of a shop, the shop(s) will be shown.
     * if there is no match, the method will print "Municipality not found. Try another municipality."
     * this method returns int y: 1 if there is a match, 0 if there is no match
     */
    static int findShop(String municipalityName, Shop[] shops) {
        int y = 0;
        for (Shop x : shops)
            if (x.municipality.equals(municipalityName.substring(0, 1).toUpperCase() + municipalityName.substring(1).toLowerCase())) {
                System.out.println(x);
                y++;
            }

        if (y == 0) {
            System.out.println("Municipality not found. Try another municipality.");
        }

        return y;
    }

    /**
     * this method takes in the parameters of the array shops, the String municipalityName (user input) and the String shopName (user input)
     * this method checks if there is a match between the municipalityName and the shopName given by the user with the indices in the array shops.
     * this method returns the respective index in the array shops. if there is no index match, it returns -1
     */
    static int findIndex(Shop[] shops, String municipalityName, String shopName) {
        for (int i = 0; i < shops.length; i++)
            if (shops[i].municipality.equals(municipalityName.substring(0, 1).toUpperCase() + municipalityName.substring(1).toLowerCase()) && shops[i].shopName.equals(shopName.substring(0, 5).toUpperCase() + shopName.substring(5).toLowerCase())) {
                return i;
            }

        return -1;
    }

    /**
     * this method takes in the parameters of String municipalityName (user input), String shopName (user input), and the array shops
     * this method calls the method findIndex to check, and if there is a matching index, continues on with the program
     * otherwise, the method prints "Shop not found. Try again."
     * this method returns the int y. if y = 1, process was successful
     */
    static int chooseShop(String municipalityName, String shopName, Shop[] shops) {
        int y = 0;
        int i = findIndex(shops, municipalityName, shopName);
        if (i < 0)
            System.out.println("Shop not found. Try again.");
        else
            y++;

        return y;
    }

    /**
     * this method takes in the parameters of String productName (user input), and the array products
     * this method checks if the product name given by the user is in the array products
     * if there is no match, it prints: "Product not found. Try another product."
     * it returns y, if successful, y = 1.
     */
    static int findProduct(String productName, Product[] products) {
        int y = 0;
        for (Product x : products)
            if (x.itemName.equals(productName)) {
                System.out.println(x);
                y++;
            }

        if (y == 0) {
            System.out.println("Product not found. Try another product.");
        }

        return y;
    }

    /**
     * this method takes in the parameters of String x (user input)
     * this method checks if the user input is yes, no, or neither.
     * this method returns y (if y = 1 user input = yes, if y = 0 user input = no, if y = 2 user input is neither)
     */
    static int shopping(String x) {
        int z;
        String a = x.substring(0, 1).toUpperCase() + x.substring(1).toLowerCase();
        if (Objects.equals(a, "Yes")) {
            z = 1;
        } else if (Objects.equals(a, "No")) {
            z = 0;
        } else {
            z = 2;
        }
        return z;
    }

    /**
     * this method takes in the parameters of double numKilos (user input), String productName (user input), and the array products
     * this method calculates and returns the cost by multiplying the number of kilos with the price of the product.
     */
    static double cost(double numKilos, String productName, Product[] products) {
        double x = 0;
        for (Product product : products) {
            if (product.itemName.equals(productName.substring(0, 1).toUpperCase() + productName.substring(1).toLowerCase())) {
                x = product.price;
            }
        }
        if (x == 0)
            System.out.println("Not a valid number. Try again. Remember to exclude the units.");
        return numKilos * x;
    }

    /**
     * this method takes in the parameters of String productName (user input), and the array products
     * this method calculates and returns the amount of product stock left.
     */
    static double productBalance(String productName, Product[] products) {
        double y = 0;
        for (Product x : products) {
            if (x.itemName.equals(productName)) {
                y = x.numKilos;
            }
        }
        return y;
    }
}

class Product {
    String itemName;
    double price, numKilos;

    Product(String itemName, double price, double numKilos) {
        this.itemName = itemName;
        this.price = price;
        this.numKilos = numKilos;
    }

    public String toString() {
        return "\t" + itemName + ": " + price + "€/kg";
    }
}

class Shop {
    String shopName, municipality, address;
    int phoneNumber;

    Shop(String shopName, String municipality, String address, int phoneNumber) {
        this.shopName = shopName;
        this.municipality = municipality;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return shopName + "\n\t" + municipality + "\n\t" + address + "\n\t" + phoneNumber + "\n\t";
    }
}

class AddToBasket {
    String productName;
    double numKilos, price;

    AddToBasket(String productName, double numKilos, double price) {
        this.productName = productName;
        this.numKilos = numKilos;
        this.price = price;
    }

    public String toString() {
        if (productName.length() < 9)
            return productName + "\t\t\t\t\t" + String.format("%.2f", numKilos) + "kg\t\t\t\t\t" + String.format("%.2f", price) + "€";
        else
            return productName + "\t\t\t\t" + String.format("%.2f", numKilos) + "kg\t\t\t\t\t" + String.format("%.2f", price) + "€";
    }
}
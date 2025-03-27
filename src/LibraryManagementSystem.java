import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryManagementSystem extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
/**
 * Initializes and starts the JavaFX application for the Digital Library Management System.
 * Sets up the UI, background, buttons, and actions for book management.
 */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Digital Library Management System");

        // --------------------------- HEADER --------------------------- //
        Label titleLabel = new Label("Library Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Set font and size
        titleLabel.setTextFill(Color.web("#2c3e50")); // Set text color

        // --------------------------- BACKGROUND IMAGE --------------------------- //
        // Define the background image path (ensure the path is correct)
        String imagePath = "file:/D:/JavaProgs/bg.jpg";
        Image backgroundImage = new Image(imagePath);
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(false); // Allow stretching to fit screen
        backgroundImageView.setSmooth(true); // Enable smooth rendering
        backgroundImageView.setCache(true); // Improve performance by caching

        // Make the image resize dynamically with the window
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());

        // --------------------------- BUTTONS --------------------------- //
        // Create buttons with custom styling
        Button addBookButton = createStyledButton("Add Book");
        Button viewBooksButton = createStyledButton("View All Books");
        Button searchBookButton = createStyledButton("Search Book");
        Button updateBookButton = createStyledButton("Update Book");
        Button deleteBookButton = createStyledButton("Delete Book");
        Button exitButton = createStyledButton("Exit");

        // --------------------------- BUTTON ACTIONS --------------------------- //
        addBookButton.setOnAction(e -> addBook());
        viewBooksButton.setOnAction(e -> viewBooks());
        searchBookButton.setOnAction(e -> searchBook());
        updateBookButton.setOnAction(e -> updateBook());
        deleteBookButton.setOnAction(e -> deleteBook());
        exitButton.setOnAction(e -> primaryStage.close()); // Close the application

        // --------------------------- BUTTON LAYOUT --------------------------- //
        VBox buttonContainer = new VBox(15); // Spacing of 15px between buttons
        buttonContainer.setPadding(new Insets(20));
        buttonContainer.setAlignment(Pos.CENTER); // Center buttons
        buttonContainer.getChildren().addAll(
                addBookButton, viewBooksButton, searchBookButton,
                updateBookButton, deleteBookButton, exitButton
        );

        // Ensure buttons are uniformly sized
        addBookButton.setPrefWidth(200);
        viewBooksButton.setPrefWidth(200);
        searchBookButton.setPrefWidth(200);
        updateBookButton.setPrefWidth(200);
        deleteBookButton.setPrefWidth(200);
        exitButton.setPrefWidth(200);

        // --------------------------- MAIN LAYOUT --------------------------- //
        BorderPane mainLayout = new BorderPane();

        // Background pane to layer image and layout
        StackPane backgroundPane = new StackPane();
        backgroundPane.getChildren().addAll(backgroundImageView, mainLayout);

        // Create a header section
        StackPane headerPane = new StackPane(titleLabel);
        headerPane.setPadding(new Insets(30, 0, 20, 0));
        headerPane.setStyle("-fx-background-color: #ecf0f1;"); // Light gray background

        // Assign layout positions
        mainLayout.setTop(headerPane); // Set header at the top
        mainLayout.setCenter(buttonContainer); // Place buttons in the center
        mainLayout.setStyle("-fx-background-color: transparent;"); // Transparent background

        // --------------------------- SCENE & STAGE SETTINGS --------------------------- //
        Scene scene = new Scene(backgroundPane, 800, 600); // Initial window size
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Start in full-screen mode
        primaryStage.show(); // Display the application window
    }

    /**
     * Creates a styled button with hover effects.
     * @param text The text to display on the button.
     * @return A styled Button object.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);

        // Default button style
        button.setStyle(
                "-fx-background-color: rgba(44, 62, 80, 0.8);" + // Dark blue background
                        "-fx-text-fill: white;" + // White text color
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-background-radius: 5px;" + // Rounded corners
                        "-fx-border-color: white;" + // White border
                        "-fx-border-width: 1px;"
        );

        // Hover effect (changes background color and adds shadow)
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: rgba(52, 152, 219, 0.9);" + // Lighter blue on hover
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-padding: 10px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: 1px;" +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.4), 10, 0, 0, 2);" // Glow effect
                )
        );

        // Reset style when mouse exits
        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: rgba(44, 62, 80, 0.8);" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 14px;" +
                                "-fx-padding: 10px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: 1px;"
                )
        );

        return button;
    }

    /**
     * Adds a row with a label and input field to a GridPane.
     * @param grid The GridPane to modify.
     * @param labelText The text for the label.
     * @param inputField The input field to place beside the label.
     * @param rowIndex The row index in the GridPane.
     */
    private void addGridRow(GridPane grid, String labelText, Node inputField, int rowIndex) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px;"); // Set font size
        grid.add(label, 0, rowIndex); // Add label to column 0
        grid.add(inputField, 1, rowIndex); // Add input field to column 1
    }


    /**
     * Displays a dialog to add a new book to the library.
     * Ensures all required fields are filled and prevents duplicate book IDs.
     */
    private void addBook() {
        // Create a dialog window for adding a new book
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter Book Details");

        // Define the "Add Book" button and a cancel button
        ButtonType addButtonType = new ButtonType("Add Book", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create a grid layout for the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create input fields for book details
        TextField idField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField genreField = new TextField();
        ComboBox<String> availabilityField = new ComboBox<>();

        // Populate the availability dropdown with options
        availabilityField.getItems().addAll("Available", "Checked Out", "Reserved", "Under Maintenance");

        // Set placeholder text for user guidance
        idField.setPromptText("Enter Book ID");
        titleField.setPromptText("Enter Title");
        authorField.setPromptText("Enter Author");
        genreField.setPromptText("Enter Genre");
        availabilityField.setPromptText("Select Availability");

        // Add labels and corresponding input fields to the grid layout
        addGridRow(grid, "Book ID:", idField, 0);
        addGridRow(grid, "Title:", titleField, 1);
        addGridRow(grid, "Author:", authorField, 2);
        addGridRow(grid, "Genre:", genreField, 3);
        addGridRow(grid, "Availability:", availabilityField, 4);

        // Set the grid layout as the content of the dialog
        dialog.getDialogPane().setContent(grid);
        idField.requestFocus(); // Focus on the ID field when the dialog opens

        // Handle user input when the "Add Book" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Retrieve and trim input values
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String genre = genreField.getText().trim();
                String availability = availabilityField.getValue();

                // Validate that no fields are empty
                if (id.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty() || availability == null) {
                    showAlert("Error", "All fields are required.");
                    return null;
                }

                // Load the existing books from the CSV file to check for duplicate IDs
                List<Book> books = readBooksFromCSV();
                for (Book book : books) {
                    if (book.getId().equals(id)) {
                        showAlert("Error", "A book with this ID already exists.");
                        return null;
                    }
                }

                // Create a new book object
                Book newBook = new Book(id, title, author, genre, availability);

                // Add the new book to the list and update the CSV file
                books.add(newBook);
                writeBooksToCSV(books);

                return newBook; // Return the new book if successfully added
            }
            return null; // Return null if the operation is canceled
        });

        // Show the dialog and wait for user interaction
        Optional<Book> result = dialog.showAndWait();

        // If a book was added, show a success message
        result.ifPresent(book -> showAlert("Success", "Book Added Successfully."));
    }


    /**
     * Displays a dialog showing all books available in the library.
     * If no books exist, it shows an alert message.
     */
    private void viewBooks() {
        // Read the list of books from the CSV file
        List<Book> books = readBooksFromCSV();

        // If no books are found, show an alert and exit the method
        if (books.isEmpty()) {
            showAlert("Information", "No books in the library yet.");
            return;
        }

        // Create a new dialog to display the list of books
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Library Books");
        dialog.setHeaderText("All Books in the Library");

        // Create a TableView to display books in a structured format
        TableView<Book> tableView = new TableView<>();
        tableView.setPrefWidth(800);  // Set preferred width
        tableView.setPrefHeight(400); // Set preferred height

        // Define table columns and map them to Book attributes

        // Column for Book ID
        TableColumn<Book, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));

        // Column for Book Title
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        // Column for Book Author
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));

        // Column for Book Genre
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));

        // Column for Book Availability Status
        TableColumn<Book, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAvailability()));

        // Add all columns to the table view
        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, genreColumn, availabilityColumn);

        // Populate the table with book data
        tableView.getItems().addAll(books);

        // Set the table view as the content of the dialog
        dialog.getDialogPane().setContent(tableView);

        // Add a close button to the dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog and wait for user interaction
        dialog.showAndWait();
    }


    /**
     * Displays a dialog allowing the user to search for a book by ID or Title.
     * If the book is found, its details are displayed; otherwise, an alert is shown.
     */
    private void searchBook() {
        // Create a new dialog for book search
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Book");
        dialog.setHeaderText("Enter Book ID or Title");

        // Create a "Search" button and add it to the dialog along with a "Cancel" button
        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        // Create a grid layout for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal spacing between elements
        grid.setVgap(10); // Vertical spacing between elements
        grid.setPadding(new Insets(20, 150, 10, 10)); // Padding around the grid

        // Create a text field for user input (Book ID or Title)
        TextField searchField = new TextField();
        searchField.setPromptText("Book ID or Title"); // Placeholder text for better UX

        // Add label and input field to the grid
        grid.add(new Label("Search:"), 0, 0);
        grid.add(searchField, 1, 0);

        // Set the grid as the content of the dialog
        dialog.getDialogPane().setContent(grid);
        searchField.requestFocus(); // Automatically focus on the input field

        // Handle result conversion when the "Search" button is clicked
        dialog.setResultConverter(dialogButton ->
                dialogButton == searchButtonType ? searchField.getText().trim() : null
        );

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();

        // Process the search query if the user provided an input
        result.ifPresent(searchTerm -> {
            List<Book> books = readBooksFromCSV(); // Retrieve book list from CSV
            for (Book book : books) {
                // Check if the book ID or title matches the search term (case insensitive)
                if (book.getId().equalsIgnoreCase(searchTerm) || book.getTitle().equalsIgnoreCase(searchTerm)) {
                    showBookDetailsDialog(book); // Show book details if found
                    return;
                }
            }
            // Show an alert if no matching book was found
            showAlert("Not Found", "No book found with the given details.");
        });
    }


    /**
     * Displays a dialog showing the details of a selected book.
     * This dialog presents all the book's information in a structured format.
     *
     * @param book The book whose details need to be displayed.
     */
    private void showBookDetailsDialog(Book book) {
        // Create a new dialog window
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Book Details");
        dialog.setHeaderText("Book Information");

        // Create a grid layout for displaying book details
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal spacing between elements
        grid.setVgap(10); // Vertical spacing between elements
        grid.setPadding(new Insets(20, 150, 10, 10)); // Padding around the grid

        // Add labels and corresponding book details to the grid
        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(book.getId()), 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(new Label(book.getTitle()), 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(new Label(book.getAuthor()), 1, 2);
        grid.add(new Label("Genre:"), 0, 3);
        grid.add(new Label(book.getGenre()), 1, 3);
        grid.add(new Label("Availability:"), 0, 4);
        grid.add(new Label(book.getAvailability()), 1, 4);

        // Set the grid as the dialog's content
        dialog.getDialogPane().setContent(grid);

        // Add a "Close" button to allow the user to close the dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog and wait for user interaction
        dialog.showAndWait();
    }


    /**
     * Handles updating a book's details.
     * The user is prompted to enter the Book ID, and if found, they can edit the book's details.
     * The updated details are saved back to the CSV file.
     */
    private void updateBook() {
        // Read the list of books from the CSV file
        List<Book> books = readBooksFromCSV();

        // Check if there are any books in the library
        if (books.isEmpty()) {
            showAlert("Information", "No books in the library to update.");
            return;
        }

        // Create a dialog for entering the Book ID
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Book");
        dialog.setHeaderText("Enter Book ID to update");

        // Add "Search" and "Cancel" buttons
        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        // Create a grid layout for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create a text field for entering the Book ID
        TextField idField = new TextField();
        idField.setPromptText("Book ID");

        // Add label and text field to the grid
        grid.add(new Label("Book ID:"), 0, 0);
        grid.add(idField, 1, 0);
        dialog.getDialogPane().setContent(grid);
        idField.requestFocus(); // Auto-focus on the input field

        // Handle the dialog result when "Search" is clicked
        dialog.setResultConverter(dialogButton -> dialogButton == searchButtonType ? idField.getText().trim() : null);

        // Show the dialog and process user input
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(id -> {
            // Search for the book by ID
            for (Book book : books) {
                if (book.getId().equals(id)) {
                    // If book is found, show the update dialog
                    showUpdateBookDialog(book);

                    // Save the updated book list back to CSV
                    writeBooksToCSV(books);

                    // Show success message
                    showAlert("Success", "Book updated successfully.");
                    return;
                }
            }

            // Show error message if book was not found
            showAlert("Not Found", "No book found with the given ID.");
        });
    }


    /**
     * Displays a dialog to update the details of an existing book.
     * The user can modify the title, author, genre, and availability status.
     * If updated, the book details are saved.
     *
     * @param book The book object to be updated.
     */
    private void showUpdateBookDialog(Book book) {
        // Create a dialog for updating book details
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Update Book");
        dialog.setHeaderText("Update Book Details");

        // Create "Update" and "Cancel" buttons
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create a grid layout for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create input fields pre-filled with the book's current details
        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField genreField = new TextField(book.getGenre());

        // Create a dropdown for book availability
        ComboBox<String> availabilityField = new ComboBox<>();
        availabilityField.getItems().addAll("Available", "Checked Out", "Reserved", "Under Maintenance");
        availabilityField.setValue(book.getAvailability()); // Set current value

        // Add labels and input fields to the grid
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(new Label("Availability:"), 0, 3);
        grid.add(availabilityField, 1, 3);

        // Set the grid as the content of the dialog
        dialog.getDialogPane().setContent(grid);
        titleField.requestFocus(); // Auto-focus on the title field

        // Handle dialog result when the user clicks "Update"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Validate inputs
                if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                        genreField.getText().isEmpty() || availabilityField.getValue() == null) {
                    showAlert("Error", "All fields are required");
                    return null; // Return null if any field is empty
                }

                // Update book details with new values
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setGenre(genreField.getText());
                book.setAvailability(availabilityField.getValue());
                return book; // Return updated book
            }
            return null; // Return null if user cancels
        });

        // Show the dialog and process the user's input
        Optional<Book> result = dialog.showAndWait();

        // If book details were updated, show success message
        result.ifPresent(updatedBook -> {
            showAlert("Success", "Book Updated Successfully");
        });
    }


    /**
     * Deletes a book from the library based on the user-provided Book ID.
     * The method reads the books from the CSV file, prompts the user for a Book ID,
     * and removes the book if it exists. The updated list is then written back to the CSV file.
     */
    private void deleteBook() {
        // Read the current list of books from the CSV file
        List<Book> books = readBooksFromCSV();

        // Check if there are any books to delete
        if (books.isEmpty()) {
            showAlert("Information", "No books in the library to delete.");
            return; // Exit the method if no books exist
        }

        // Create a dialog to ask the user for the Book ID to delete
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete Book");
        dialog.setHeaderText("Enter Book ID to delete");

        // Create the "Delete" and "Cancel" buttons
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        // Create a grid layout for input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create an input field for Book ID
        TextField idField = new TextField();
        idField.setPromptText("Book ID"); // Set a placeholder text

        // Add label and input field to the grid
        grid.add(new Label("Book ID:"), 0, 0);
        grid.add(idField, 1, 0);

        // Set the grid as the content of the dialog
        dialog.getDialogPane().setContent(grid);
        idField.requestFocus(); // Auto-focus on the input field

        // Handle the dialog result (when the user clicks "Delete")
        dialog.setResultConverter(dialogButton -> dialogButton == deleteButtonType ? idField.getText().trim() : null);

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();

        // If a Book ID is provided, proceed with deletion
        result.ifPresent(id -> {
            int initialSize = books.size(); // Store the initial number of books

            // Remove the book with the given ID from the list
            books.removeIf(book -> book.getId().equals(id));

            // Check if any book was actually removed
            if (books.size() < initialSize) {
                writeBooksToCSV(books); // Update the CSV file with the new book list
                showAlert("Success", "Book deleted successfully.");
            } else {
                showAlert("Not Found", "No book found with the given ID."); // Show an error if no match was found
            }
        });
    }


    // Define the CSV file name
    private static final String CSV_FILE = "books.csv";

    /**
     * Reads books from the CSV file and returns a list of Book objects.
     * Each line in the CSV is split into 5 attributes (ID, Title, Author, Genre, Availability).
     */
    private List<Book> readBooksFromCSV() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split CSV line by commas
                if (data.length == 5) { // Ensure data integrity
                    books.add(new Book(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        return books;
    }

    /**
     * Writes the list of books to the CSV file.
     * Each book's attributes are joined by commas and written as a new line in the file.
     */
    private void writeBooksToCSV(List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Book book : books) {
                bw.write(String.join(",", book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getAvailability()));
                bw.newLine(); // Move to next line for each book
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the given title and message.
     * Used for showing success, error, and information messages to the user.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The Book class represents a book in the library.
     * It stores details such as ID, Title, Author, Genre, and Availability status.
     */
    class Book {
        private String id, title, author, genre, availability;

        /**
         * Constructor to initialize a Book object with its details.
         */
        public Book(String id, String title, String author, String genre, String availability) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.availability = availability;
        }

        // Getter methods to retrieve book details
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getGenre() { return genre; }
        public String getAvailability() { return availability; }

        // Setter methods to update book details
        public void setTitle(String title) { this.title = title; }
        public void setAuthor(String author) { this.author = author; }
        public void setGenre(String genre) { this.genre = genre; }
        public void setAvailability(String availability) { this.availability = availability; }

        /**
         * Returns a string representation of the book, useful for debugging or displaying book details.
         */
        @Override
        public String toString() {
            return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Genre: " + genre + ", Availability: " + availability;
        }
    }

}

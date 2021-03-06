package com.s0qva.application.controller.admin;

import com.s0qva.application.controller.eventhandler.DefaultUserAccountEventHandler;
import com.s0qva.application.controller.ProductController;
import com.s0qva.application.controller.scene.SceneSwitcher;
import com.s0qva.application.dto.product.ProductCreationDto;
import com.s0qva.application.dto.product.ProductReadingDto;
import com.s0qva.application.dto.product.detail.ProductDetailsCreationDto;
import com.s0qva.application.fxml.FxmlPageLoader;
import com.s0qva.application.model.enumeration.Country;
import com.s0qva.application.service.ProductService;
import com.s0qva.application.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FxmlView("products-admin-page.fxml")
@Component
public class ProductAdminController extends ProductController implements Initializable {
    private final DefaultUserAccountEventHandler defaultUserAccountEventHandler;
    private final Class<MainAdminPageController> mainAdminPageControllerClass;
    @FXML
    private ListView<ProductReadingDto> products;
    @FXML
    private HBox account;
    @FXML
    private VBox windowsForCreating;
    @FXML
    private TextField productName;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productDescription;
    @FXML
    private ComboBox<Country> productMadeInComboBox;

    @Autowired
    public ProductAdminController(ProductService productService,
                                  FxmlPageLoader fxmlPageLoader,
                                  DefaultUserAccountEventHandler defaultUserAccountEventHandler) {
        super(productService, fxmlPageLoader);
        this.defaultUserAccountEventHandler = defaultUserAccountEventHandler;
        this.mainAdminPageControllerClass = MainAdminPageController.class;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultUserAccountEventHandler.addEventHandlerToShowUserAccount(account);
        addEventToShowProductDetails(products);
        fillProductMadeInComboBox();
    }

    public void onReceiveAllProducts() {
        List<ProductReadingDto> receivedProducts = productService.getAllProducts();
        products.setItems(FXCollections.observableArrayList(receivedProducts));
    }

    public void onCreateNewProduct() {
        windowsForCreating.setVisible(true);
    }

    public void onConfirmCreatingProduct() {
        windowsForCreating.setVisible(false);

        ProductCreationDto newProduct = buildProductCreationDto();
        boolean isCreated = productService.createProduct(newProduct);

        if (isCreated) {
            AlertUtil.generateInformationAlert(
                    DefaultAlertValue.INFO_ALERT_CREATED_PRODUCT_TITLE,
                    DefaultAlertValue.INFO_ALERT_CREATED_PRODUCT_HEADER,
                    DefaultAlertValue.INFO_ALERT_CREATED_PRODUCT_CONTENT
            );
            clearAllProductCreatingFields();
            onReceiveAllProducts();
        }
    }

    public void onCancelCreatingProduct() {
        windowsForCreating.setVisible(false);
        clearAllProductCreatingFields();
    }

    private void fillProductMadeInComboBox() {
        productMadeInComboBox.getItems().addAll(Country.values());
    }

    private void clearAllProductCreatingFields() {
        productName.clear();
        productPrice.clear();
        productDescription.clear();
        productMadeInComboBox.getSelectionModel().clearSelection();
    }

    public void onBackToMainAdminPage(ActionEvent event) {
        Parent root = fxmlPageLoader.loadFxmlFile(mainAdminPageControllerClass);
        SceneSwitcher.switchScene(event, root);
    }

    private ProductCreationDto buildProductCreationDto() {
        ProductDetailsCreationDto productDetailsCreationDto = null;
        String description = productDescription.getText();
        Country selectedCountry = productMadeInComboBox.getSelectionModel().getSelectedItem();

        if (!description.isBlank() || selectedCountry != null) {
            productDetailsCreationDto = new ProductDetailsCreationDto();

            if (!description.isBlank()) {
                productDetailsCreationDto.setDescription(description);
            }
            if (selectedCountry != null) {
                productDetailsCreationDto.setMadeIn(selectedCountry);
            }
        }

        ProductCreationDto productCreationDto = ProductCreationDto.builder()
                .name(productName.getText())
                .price(Double.valueOf(productPrice.getText()))
                .build();

        if (productDetailsCreationDto != null) {
            productCreationDto.setDetails(productDetailsCreationDto);
        }

        return productCreationDto;
    }

    private static class DefaultAlertValue {
        private static final String INFO_ALERT_CREATED_PRODUCT_TITLE = "Successful product creation";
        private static final String INFO_ALERT_CREATED_PRODUCT_HEADER = "The product has been successfully created!";
        private static final String INFO_ALERT_CREATED_PRODUCT_CONTENT = "The product is available for purchase";
    }
}

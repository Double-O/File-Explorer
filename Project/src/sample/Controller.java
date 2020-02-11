package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    public String currentDirectory = System.getProperty("user.dir");
    public static File[] ini_pths = File.listRoots();
    public int isTableViewOrTileView = 0;

    public BorderPane pb;

    //String
    //public TreeItem<String> noderoot = new TreeItem<>("MY PC");
    public TreeItem<TableObject> noderoot = new TreeItem<>(new TableObject(null ,new File("MY PC")));
    public TreeView treeview = new TreeView();


    public TableView < TableObject > tableview = new TableView<>();
    public TableColumn<TableObject, String> NameColumn = new TableColumn<>("Name");
    public TableColumn<TableObject, String> DateModifiedColumn = new TableColumn<>("DateModified");
    public TableColumn<TableObject, Integer> SizeColumn = new TableColumn<>("Size");
    public TableColumn<TableObject, ImageView> ImageViewColumn = new TableColumn<>("Icon");


    public Button TableOrListView = new Button();
    public Button BackButton = new Button();


    public ImageView im;

    public FlowPane tileview = new FlowPane();
    public ScrollPane scrollpane = new ScrollPane();

    public TextField txtfield = new TextField();


    public void something(Event e){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        MakeTable(currentDirectory);
        pb.setMinWidth(700);
        pb.setMinHeight(300);

        txtfield.setEditable(false);
        txtfield.setText(currentDirectory);

        TableOrListView.setText("TileView");

        getSelectedItemFromTree();
        getselectedRowFromTable();

        TableOrListView.setOnAction(e -> {

            if(isTableViewOrTileView == 0) {
                isTableViewOrTileView = 1;
                TableOrListView.setText("TableView");
                MakeTile(currentDirectory);
            }


            else{
                isTableViewOrTileView = 0;
                TableOrListView.setText("TileView");
                MakeTable(currentDirectory);
            }


        });

        BackButton.setOnAction(e->{


            String tempcurrentDirectory = new File(currentDirectory).getParent();

            if(tempcurrentDirectory != null) {
                currentDirectory = tempcurrentDirectory;
                txtfield.setText(currentDirectory);
                if (isTableViewOrTileView == 0) MakeTable(currentDirectory);
                else MakeTile(currentDirectory);

            }



        });


        //String
        for(File p : ini_pths){

            TreeItem < TableObject > temp = new TreeItem<>(new TableObject(p), new ImageView(getIcon(p)));
            //TreeItem<String> temp = new TreeItem<>(p.toString(), new ImageView(getIcon(p)));
            MakeEdge(temp, noderoot);
        }




        NameColumn.setMinWidth(100);
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("RealName"));


        DateModifiedColumn.setMinWidth(70);
        DateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("DateModified"));


        SizeColumn.setMinWidth(70);
        SizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));


        ImageViewColumn.setMinWidth(70);
        ImageViewColumn.setCellValueFactory(new PropertyValueFactory<>("img"));

        tableview.setMinWidth(400);
        tableview.getColumns().addAll(ImageViewColumn, NameColumn, SizeColumn, DateModifiedColumn);

        treeview.setRoot(noderoot);








    }





    //String
    public void MakeEdge(TreeItem<TableObject> chld, TreeItem<TableObject> prnt){
        prnt.getChildren().add(chld);
    }

    public Image getIcon(File some){
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(some);
        BufferedImage bufferedImage = (BufferedImage)icon.getImage();
        WritableImage fxImage = new WritableImage(icon.getIconWidth(), icon.getIconHeight());
        Image imgg = SwingFXUtils.toFXImage(bufferedImage, fxImage);

        return imgg;
    }


    //String
    public void getSelectedItemFromTree(){

        treeview.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {

                //TreeItem<TableObject> selectedItem = (TreeItem<TableObject>) newValue;
                //currentDirectory = selectedItem.getValue();

                TreeItem <TableObject> selectedItem = (TreeItem<TableObject>) newValue;
                File temp = new File(selectedItem.getValue().getName());

                if(temp.isDirectory() == true) {

                    //System.out.println(fl.toPath().getFileName());
                    currentDirectory = selectedItem.getValue().getName();

                    txtfield.setText(currentDirectory);

                    if (isTableViewOrTileView == 0) MakeTable(currentDirectory);
                    else MakeTile(currentDirectory);

                    if (selectedItem.getChildren().size() == 0) MakeNodes(selectedItem);
                }

            }

        });

    }

    public void getselectedRowFromTable(){

        tableview.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = tableview.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
                int ind = tablePosition.getRow();

                ObservableList<TableObject> items = tableview.getItems();
                if(ind >= items.size()) return;

                TableObject tb = items.get(ind);

                File temp = new File(tb.getName());
                if(temp.isDirectory() == true) {

                    currentDirectory = tb.getName();
                    txtfield.setText(currentDirectory);
                    MakeTable(currentDirectory);

                }
            }
        });




    }


    //String
    public void MakeNodes(TreeItem <TableObject> cur){


        //File currentFile = new File(cur.getValue());

        File currentFile = new File(cur.getValue().getName());

        if(currentFile.isDirectory() == false) return;

        File[] files = currentFile.listFiles();

        if(files.length == 0) return;

        for(File f: files){
            //TreeItem < String > tmp = new TreeItem<>(f.toString(), new ImageView(getIcon(f)));
            TreeItem<TableObject> tmp = new TreeItem<>(new TableObject(f), new ImageView(getIcon(f)));
            MakeEdge(tmp, cur);
        }

        cur.setExpanded(true);


    }

    public void MakeTable(String Current){

        File tmpp = new File(Current);

        if(tmpp.isDirectory() == false) return;

        tableview.getItems().clear();
        File[] files = tmpp.listFiles();

        ObservableList < TableObject > temp = FXCollections.observableArrayList();
        if(files.length != 0) temp = getObservableList(files);

        tableview.setItems(temp);

        pb.setCenter(tableview);


        tableview.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

    }

    public ObservableList<TableObject> getObservableList(File[] files) {

        ObservableList <TableObject> temp = FXCollections.observableArrayList();
        if(files.length == 0) return temp;
        for(File f: files){

           // System.out.println(f.toString());

            temp.add(new TableObject(f));
        }

        return temp;


    }

    public void MakeTile(String current){

        File tmpp = new File(current);


        tileview.getChildren().clear();

        tileview.setHgap(5);
        tileview.setVgap(5);

        tileview.setAlignment(Pos.TOP_LEFT);



        if(tmpp.isDirectory() == false) return;

        File[] files = tmpp.listFiles();

        for(File f: files){

            ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(f);
            BufferedImage bufferedImage = (BufferedImage)icon.getImage();
            WritableImage fxImage = new WritableImage(icon.getIconWidth(), icon.getIconHeight());
            Image imgg = SwingFXUtils.toFXImage(bufferedImage, fxImage);

            ImageView Img = new ImageView(imgg);

            Img.setFitHeight(25);
            Img.setFitWidth(25);

            Label lbl = new Label(f.toPath().getFileName().toString());
            lbl.setGraphic(Img);

            lbl.setPrefSize(80, 30);

            lbl.setAlignment(Pos.CENTER);

            lbl.setContentDisplay(ContentDisplay.TOP);
            lbl.setWrapText(true);
            lbl.setId(f.toString());

            lbl.setOnMouseClicked(e->{

                File tmp = new File(lbl.getId());
                if(tmp.isDirectory() == true) {

                    currentDirectory = lbl.getId();
                    MakeTile(currentDirectory);

                }
            });

            tileview.getChildren().add(lbl);


        }

        scrollpane.setContent(tileview);

        pb.setCenter(scrollpane);

        tileview.setPrefSize(600, 300);


    }



}


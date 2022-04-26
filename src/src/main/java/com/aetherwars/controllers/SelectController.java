// package com.aetherwars.controllers;


// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.scene.control.Button;
// import javafx.scene.layout.BackgroundSize;
// import javafx.stage.FileChooser;

// public class SelectController  {
//     public Button import_btn;
//     public Button random_btn;

//     final FileChooser chooser1 = new FileChooser();
//     final FileChooser chooser2 = new FileChooser();
//     final BackgroundSize bgSize = new BackgroundSize(
//             0.9, 0.9, true, true,
//             true, false
//     );


//     EventHandler<ActionEvent> random_evt = new EventHandler<ActionEvent>(){ 
//         @Override
//         public void handle(ActionEvent event) {
//             configureFileChooser(chooser1);
//             File file = chooser1.showOpenDialog(stage);
//             if (file != null) {
//                 openFile(file);
//             }
//         }
//     };

//     EventHandler<ActionEvent> import_evt = new EventHandler<ActionEvent>(){ 
//         @Override
//         public void handle(ActionEvent event) {
//             getDeckFile(true);
//         }
//     };
    

//     // public SelectController(){
//     //     import_btn.setOnAction();

//     // }


// }

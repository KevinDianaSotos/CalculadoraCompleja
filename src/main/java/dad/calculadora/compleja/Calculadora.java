package dad.calculadora.compleja;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Calculadora extends Application {
	//CREAMOS LOS DATOS RELACIONADOS CON COMPLEJO() Y EL STRING PROPERTY DE OPERADOR
	private Complejo primerNumero = new Complejo();
	private Complejo segundoNumero = new Complejo();
	private Complejo tercerNumero = new Complejo();
	private Complejo cuartoNumero = new Complejo();
	private Complejo quintoNumero = new Complejo();
	private Complejo sextoNumero = new Complejo();
	private StringProperty operador = new SimpleStringProperty();
	
	
	//CREAMOS LOS TEXTFIELDS DE LOS NUMEROS DE LA CALCULADORA
	private TextField primerNumeroText;
	private TextField segundoNumeroText;
	private TextField tercerNumeroText;
	private TextField cuartoNumeroText;
	private TextField quintoNumeroText;
	private TextField sextoNumeroText;
	private ComboBox<String> operadorCombo;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		//CREAMOS LOS TEXTFIELD PARA LA VISTA
		primerNumeroText = new TextField();
		primerNumeroText.setPrefColumnCount(4);
		
		//CREAMOS LOS TEXTFIELD PARA LA VISTA
		segundoNumeroText = new TextField();
		segundoNumeroText.setPrefColumnCount(4);
		
		//CREAMOS LOS TEXTFIELD PARA LA VISTA
		tercerNumeroText = new TextField();
		tercerNumeroText.setPrefColumnCount(4);
		
		//CREAMOS LOS TEXTFIELD PARA LA VISTA
		cuartoNumeroText = new TextField();
		cuartoNumeroText.setPrefColumnCount(4);
		
		//CREAMOS LOS TEXTFIELD PARA LA VISTA
		quintoNumeroText = new TextField();
		quintoNumeroText.setPrefColumnCount(4);
		quintoNumeroText.setDisable(true);//DESHABILITAMOS AMBOS
		
		sextoNumeroText = new TextField();
		sextoNumeroText.setPrefColumnCount(4);
		sextoNumeroText.setDisable(true);//DESHABILITAMOS AMBOS
		
		operadorCombo = new ComboBox<String>();//CREAMOS EL COMBOBOX DE STRINGS
		operadorCombo.getItems().addAll("+", "-", "*", "/");//AÑADIMOS AL COMBO TODAS LAS OPERACIONES POSIBLES
		
		HBox arriba = new HBox(4, primerNumeroText, new Label("+"), segundoNumeroText, new Label("i"));//CREAMOS LOS HBOX CORRESPONDIENTES , ESTE PARA LA PRIMERA FILA
		HBox medio = new HBox(4, tercerNumeroText, new Label("+"), cuartoNumeroText, new Label("i"));//CREAMOS LOS HBOX CORRESPONDIENTES , ESTE PARA LA SEGUNDA FILA
		HBox abajo = new HBox(4, quintoNumeroText, new Label("+"), sextoNumeroText, new Label("i"));//CREAMOS LOS HBOX CORRESPONDIENTES , ESTE PARA LA TERCERA FILA
		
		VBox primerVBox = new VBox(1, operadorCombo);//EN EL PRIMER VBOX AÑADIMOS LOS OPERADORES
		primerVBox.setAlignment(Pos.CENTER);//LO ALINEAMOS AL CENTRO DEL VBOX
		VBox segundoVBox = new VBox(4, arriba, medio, new Separator(), abajo);//SIGUIENTE VBOX CON PRIMEROS NUMEROS , SEGUNDOS , SEPARADOR Y RESULTADOS
		segundoVBox.setAlignment(Pos.CENTER); // LO POSICIONAMOS AL CENTRO
		
		HBox root = new HBox(2, primerVBox, segundoVBox);//CREAMOS UN HBOX QUE ENGLOBE AMBOS VBOX
		root.setAlignment(Pos.CENTER);//Y ALINEMOS AL CENTRO
		
		Scene scene = new Scene(root, 320, 200);//CREAMOS LA ESCENA

		primaryStage.setScene(scene);
		primaryStage.setTitle("Calculadora Compleja FX");
		primaryStage.show();//Y LA ENSEÑAMOS
		
		//PRIMERO REALIZAMOS LOS BINDEOS BIDIRECCIONALES DE TODOS LOS NUMEROS
		Bindings.bindBidirectional(primerNumeroText.textProperty(), primerNumero.realProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(segundoNumeroText.textProperty(), segundoNumero.imaginarioProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(tercerNumeroText.textProperty(), tercerNumero.realProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(cuartoNumeroText.textProperty(), cuartoNumero.imaginarioProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(quintoNumeroText.textProperty(), quintoNumero.realProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(sextoNumeroText.textProperty(), sextoNumero.imaginarioProperty(), new NumberStringConverter());
		operador.bind(operadorCombo.getSelectionModel().selectedItemProperty());
		
		//HACEMOS UN LISTENER  POR SI EL OPERADOR CAMBIA
		operador.addListener((o, ov, nv) -> onOperadorChanged(nv));
		operadorCombo.getSelectionModel().selectFirst(); // POR DEFECTO COJA EL PRIMER VALOR DE COMBO BOX
	}
	
	//CREAMOS LA FUNCION PARA EL FUNCIONAMIENTO DE LA CALCULADORA
	private void onOperadorChanged(String nv) {

		switch(nv) {
		case "+"://EN CASO DE QUE EL OPERADOR SEA UNA SUMA
			quintoNumero.realProperty().bind(primerNumero.realProperty().add(tercerNumero.realProperty()));//A+C
			sextoNumero.imaginarioProperty().bind(segundoNumero.imaginarioProperty().add(cuartoNumero.imaginarioProperty()));//B+D
			break;
		case "-":
			quintoNumero.realProperty().bind(primerNumero.realProperty().subtract(tercerNumero.realProperty()));//A-C
			sextoNumero.imaginarioProperty().bind(segundoNumero.imaginarioProperty().subtract(cuartoNumero.imaginarioProperty()));//B-D
			break;
		case "*": 
			quintoNumero.realProperty().bind(primerNumero.realProperty().multiply(tercerNumero.realProperty()).subtract(segundoNumero.imaginarioProperty().multiply(cuartoNumero.imaginarioProperty())));//(A*C)-(B*D)
			sextoNumero.imaginarioProperty().bind(primerNumero.realProperty().multiply(cuartoNumero.imaginarioProperty()).add(segundoNumero.imaginarioProperty().multiply(tercerNumero.realProperty())));//(A*D)+(B*C)
			break;
		case "/":
			quintoNumero.realProperty().bind(((primerNumero.realProperty().multiply(tercerNumero.realProperty())).add(segundoNumero.imaginarioProperty().multiply(cuartoNumero.imaginarioProperty()))).divide((tercerNumero.realProperty().multiply(tercerNumero.realProperty())).add((cuartoNumero.imaginarioProperty()).multiply(cuartoNumero.imaginarioProperty()))));
			// ( (A*C) + (B*D) ) / (Cª+Dª) 
			sextoNumero.imaginarioProperty().bind(((segundoNumero.imaginarioProperty().multiply(tercerNumero.realProperty())).subtract(primerNumero.realProperty().multiply(cuartoNumero.imaginarioProperty()))).divide((tercerNumero.realProperty().multiply(tercerNumero.realProperty())).add((cuartoNumero.imaginarioProperty()).multiply(cuartoNumero.imaginarioProperty()))));
			//( (B*C) - (A*D) ) / (Cª+Dª) 
			break;
		}
		
	}

	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}

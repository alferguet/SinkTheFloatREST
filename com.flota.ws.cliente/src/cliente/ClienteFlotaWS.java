package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class ClienteFlotaWS {

	public static final int NUMFILAS = 8, NUMCOLUMNAS = 8, NUMBARCOS = 6;

	  private int quedan = NUMBARCOS, disparos = 0;

	  private GuiTablero guiTablero = null;
	  private GestorPartidas gestor = null;

	  public static void main(String args[]) {
	   ClienteFlotaWS cliente = new ClienteFlotaWS();
	   cliente.conecta();
	  } //end main

	  private void conecta() {
	   gestor = new GestorPartidas();
	   gestor.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
	      SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	          guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
	          guiTablero.dibujaTablero();
	        }
	      });
	  }
	  class GuiTablero {

	    private int numFilas, numColumnas;

	    private JFrame frame = null; // Tablero de juego
	    private JLabel estado = null; // Texto en el panel de estado
	    private JButton buttons[][] = null; // Botones asociados a las casillas
	    // de la partida

	    /**
	     * Constructor de una tablero dadas sus dimensiones
	     */
	    GuiTablero(int numFilas, int numColumnas) {
	      this.numFilas = numFilas;
	      this.numColumnas = numColumnas;
	      frame = new JFrame();
	      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    }

	    /**
	     * Dibuja el tablero de juego y crea la partida inicial
	     */
	    public void dibujaTablero() {
	      anyadeMenu();
	      anyadeGrid(numFilas, numColumnas);
	      anyadePanelEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
	      frame.setSize(300, 300);
	      frame.setVisible(true);
	    } // end dibujaTablero

	    /**
	     * Anyade el menu de opciones del juego y le asocia un escuchador
	     */
	    private void anyadeMenu() { //COMENTAR
	      MenuListener mlist = new MenuListener();
	      JMenuBar menuBar;
	      JMenu menu;
	      JMenuItem mossol, npar, salir;
	      menuBar = new JMenuBar();
	      menu = new JMenu("Opciones");
	      menuBar.add(menu);
	      mossol = new JMenuItem("Mostrar Solucion");
	      npar = new JMenuItem("Nueva Partida");
	      salir = new JMenuItem("Salir");
	      menu.add(mossol);
	      menu.add(npar);
	      menu.add(salir);
	      mossol.addActionListener(mlist);
	      npar.addActionListener(mlist);
	      salir.addActionListener(mlist);
	      frame.getContentPane().add(menuBar, BorderLayout.NORTH);
	    } // end anyadeMenu

	    /**
	     * Anyade el panel con las casillas del mar y sus etiquetas. Cada
	     * casilla sera un boton con su correspondiente escuchador
	     *
	     * @param nf
	     *            numero de filas
	     * @param nc
	     *            numero de columnas
	     */
	    private void anyadeGrid(int nf, int nc) { //COMENTAR
	      buttons = new JButton[nf][nc];
	      ButtonListener blist = new ButtonListener();
	      GridLayout centroLay = new GridLayout(nf + 1, nc + 2);
	      JPanel centro = new JPanel();
	      centro.setLayout(centroLay);
	      centro.add(new JLabel(" "));
	      for (int i = 1; i <= nc; i++) {
	        centro.add(new JLabel(Integer.toString(i), SwingConstants.CENTER));
	      }
	      centro.add(new JLabel(" "));
	      char letra = 'A';
	      for (int i = 0; i < nf; i++) {
	        centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));

	        for (int n = 0; n < nc; n++) {
	          buttons[i][n] = new JButton();
	          buttons[i][n].addActionListener(blist);
	          buttons[i][n].putClientProperty("fila", i);
	          buttons[i][n].putClientProperty("columna", n);
	          centro.add(buttons[i][n]);
	        }
	        centro.add(new JLabel(String.valueOf((letra)), SwingConstants.CENTER));
	        letra++;
	      }

	      frame.getContentPane().add(centro, BorderLayout.CENTER);

	    }

	    /**
	     * Anyade el panel de estado al tablero
	     *
	     * @param cadena
	     *            cadena inicial del panel de estado
	     */
	    private void anyadePanelEstado(String cadena) { // DONE
	      JPanel panelEstado = new JPanel();
	      estado = new JLabel(cadena);
	      panelEstado.add(estado);
	      // El panel de estado queda en la posición SOUTH del frame
	      frame.getContentPane().add(panelEstado, BorderLayout.SOUTH);
	    } // end anyadePanel Estado

	    /**
	     * Cambia la cadena mostrada en el panel de estado
	     *
	     * @param cadenaEstado
	     *            nuevo estado
	     */
	    public void cambiaEstado(String cadenaEstado) { // DONE
	      estado.setText(cadenaEstado);
	    } // end cambiaEstado

	    /**
	     * Muestra la solucion de la partida y marca la partida como finalizada
	     */
	    public void muestraSolucion() {
	    	Color colorAzul = new Color(0, 100, 255);// clase Color de Java, utilizada para pintar botones
            // POR IMPLEMENTAR
            String[] cadenaBarcos;
            
                cadenaBarcos = gestor.getSolucion();

                int nf = this.numFilas;
                int nc = this.numColumnas;
                // pinto todos los botones de azul

                for (int i = 0; i < nf; i++) {

                    for (int j = 0; j < nc; j++) {
                        pintaBoton(buttons[i][j], colorAzul);

                    }
                }
                for (String barco : cadenaBarcos) {

                    pintaBarcoHundido(barco);// llamada a pintarBarcoHundido
                }
            

	    } // end muestraSolucion

	    /**
	     * Pinta un barco como hundido en el tablero
	     *
	     * @param cadenaBarco
	     *            cadena con los datos del barco codifificados como
	     *            "filaInicial#columnaInicial#orientacion#tamanyo"
	     */
	    public void pintaBarcoHundido(String cadenaBarco) {
	    	System.out.println(cadenaBarco);
	      // Una vez hundido se encarga de pintarlo segun orientacion
	      String[] partes = cadenaBarco.split("#");

	      int fInicial = Integer.parseInt(partes[0]);
	      int cInicial = Integer.parseInt(partes[1]);
	      String orientacion = partes[2];
	      int tamayo = Integer.parseInt(partes[3]);

	      if (orientacion.equals("V")) {
	        for (int i = 0; i < tamayo; i++) {
	          pintaBoton(buttons[fInicial + i][cInicial], Color.RED);
	        }
	      } else {
	        for (int i = 0; i < tamayo; i++) {
	          pintaBoton(buttons[fInicial][cInicial + i], Color.RED);
	        }
	      }
	    }

	    // end pintaBarcoHundido

	    /**
	     * Pinta un botón de un color dado
	     *
	     * @param b
	     *            boton a pintar
	     * @param color
	     *            color a usar
	     */
	    public void pintaBoton(JButton b, Color color) {
	      b.setBackground(color);
	      // El siguiente código solo es necesario en Mac OS X
	      b.setOpaque(true);
	      b.setBorderPainted(false);
	    } // end pintaBoton

	    /**
	     * Limpia las casillas del tablero pintándolas del gris por defecto
	     */
	    public void limpiaTablero() {
	      for (int i = 0; i < numFilas; i++) {
	        for (int j = 0; j < numColumnas; j++) {
	          buttons[i][j].setBackground(null);
	          buttons[i][j].setOpaque(true);
	          buttons[i][j].setBorderPainted(true);
	        }
	      }
	    } // end limpiaTablero

	    /**
	     * Destruye y libera la memoria de todos los componentes del frame
	     */
	    public void liberaRecursos() {
	      frame.dispose();
	    } // end liberaRecursos

	  } // end class GuiTablero

	  /******************************************************************************************/
	  /********************* * CLASE INTERNA MenuListener * ***************************************/
	  /******************************************************************************************/

	  /**
	   * Clase interna que escucha el menu de Opciones del tablero
	   */
	  private class MenuListener implements ActionListener {

	    @Override
	    public void actionPerformed(ActionEvent e) { // Segun elemento menu
	      // realiza accion
	      JMenuItem elem = (JMenuItem) e.getSource();
	      String texto = elem.getText();
	      if (texto.equals("Mostrar Solucion")) {
	        guiTablero.muestraSolucion();
	      } else if (texto.equals("Nueva Partida")) {
	        gestor.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);;
	        // Resetea el juego a partir de aqui
	        guiTablero.limpiaTablero();
	        quedan = NUMBARCOS;
	        disparos = 0;
	        guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
	      } else if (texto.equals("Salir")) {
	        guiTablero.liberaRecursos();
	        System.exit(0);
	      }

	    } // end actionPerformed

	  } // end class MenuListener

	  /******************************************************************************************/
	  /********************* * CLASE INTERNA ButtonListener * *************************************/
	  /******************************************************************************************/
	  /**
	   * Clase interna que escucha cada uno de los botones del tablero Para poder
	   * identificar el boton que ha generado el evento se pueden usar las
	   * propiedades de los componentes, apoyandose en los metodos
	   * putClientProperty y getClientProperty
	   */
	  private class ButtonListener implements ActionListener { // Segun casilla la
	    // prueba si no
	    // ha sido ya
	    // tocada

	    @Override
	    public void actionPerformed(ActionEvent e) {
	      JButton boton = new JButton();
	      Color colorDefault = boton.getBackground();
	      boton = (JButton) e.getSource();

	      if (quedan > 0 && boton.getBackground().equals(colorDefault)) {
	        int fila = (int) boton.getClientProperty("fila");
	        int columna = (int) boton.getClientProperty("columna");
	        
	        int casilla = gestor.pruebaCasilla(fila, columna);
	        System.out.println(casilla);
	        if (casilla == -1)
	          guiTablero.pintaBoton(boton, Color.CYAN);
	        else if (casilla == -2)
	          guiTablero.pintaBoton(boton, Color.ORANGE);
	        else {
	        	System.out.println("Hundo: "+casilla);
	          guiTablero.pintaBarcoHundido(gestor.getBarco(casilla));
	          quedan--;
	        }
	        disparos++;

	        if (quedan == 0) {
	          guiTablero.cambiaEstado("Game Over con disparos: " + disparos);

	        } else {
	          guiTablero.cambiaEstado("Intentos: " + disparos + "    Barcos restantes: " + quedan);
	        }
	      }
	      // end actionPerformed

	    } // end class ButtonListener
	  }

}

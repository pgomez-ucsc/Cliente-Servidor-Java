import java.net.*;
import java.io.*;


public class Servidor {

	static ServerSocket servidorSocket = null;

	public static void main(String[] args) throws IOException {

		if (args.length != 1) { //si hay más de 1 parámetro
			System.out.println("Ingresar solo 1 argumento, el número del puerto donde el servicio esta escuchando.");
			System.exit(-1);
		} else {
		    System.out.println("El servicio se va a configurar en el puerto " + args[0]+" ...");
		}
		Socket clienteSocket=null;
		DataOutputStream mensajeSalidaDelServidor = null;
		DataInputStream mensajeEntradaAlServidor = null;
		Integer servicioPuerto = Integer.parseInt(args[0]);
		String entradaRemota="";
		boolean escuchando = true;

		try {
			System.out.println("Creando el servicio en el puerto "+ servicioPuerto+" ...");
			servidorSocket = new ServerSocket(servicioPuerto);
			System.out.println("Servicio en el puerto "+ servicioPuerto+" creado exitosamente ...");
		} catch (IOException e) {
			System.err.println("No se puede utilizar el puerto: "+servicioPuerto);
			System.exit(-1);
		}

		System.out.println("Servidor listo para aceptar requerimiento de clientes ....");


		Runtime.getRuntime().addShutdownHook(new Thread() {
		        public void run() {
							System.out.println("Cerrando servicio ....");
							try{
								servidorSocket.close();
								System.out.println("Servicio cerrado.");
							} catch (IOException e) {
								e.printStackTrace();
							}
		        }
		    });


				while (escuchando) {
					System.out.println("Escuchando ....");
					clienteSocket=servidorSocket.accept();
					System.out.println("se conecto un cliente desde: "+ clienteSocket.getInetAddress());
					try {
						mensajeSalidaDelServidor = new DataOutputStream(clienteSocket.getOutputStream());
						mensajeEntradaAlServidor = new DataInputStream(clienteSocket.getInputStream());

						mensajeSalidaDelServidor.writeUTF("El servidor dice que esta OK para recibir mensajes...");
						System.out.println("Esperando mensajes de desde el cliente ...");

						while ((entradaRemota = mensajeEntradaAlServidor.readUTF()) != null) {
							System.out.println("Llego desde el cliente el mensaje --> " +entradaRemota);
							mensajeSalidaDelServidor.writeUTF("mensaje <<" + entradaRemota + ">> recibido.");

						}
					} catch (IOException e) {
						System.out.println("Cliente " +clienteSocket.getInetAddress()+ " se desconecto.");
						mensajeSalidaDelServidor.close();
						mensajeEntradaAlServidor.close();
						clienteSocket.close();
						//e.printStackTrace();
					}
				}
	}
}

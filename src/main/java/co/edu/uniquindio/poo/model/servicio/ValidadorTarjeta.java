package co.edu.uniquindio.poo.model.servicio;

public class ValidadorTarjeta {

    public boolean esValida(String numeroTarjeta) {
        if (numeroTarjeta == null)
            return false;
        String numero = numeroTarjeta.replace(" ", "");
        if (numero.length() < 13 || numero.length() > 19 || !numero.matches("\\d+")) {
            return false;
        }

        int suma = 0;
        boolean duplicar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = numero.charAt(i) - '0';
            if (duplicar) {
                digito *= 2;
                if (digito > 9)
                    digito -= 9;
            }
            suma += digito;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}

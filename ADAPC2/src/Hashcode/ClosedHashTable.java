package Hashcode;

public class ClosedHashTable {
    private ClosedHashEntry[] tabla;
    private int size;

    public ClosedHashTable(int capacidad) {
        tabla = new ClosedHashEntry[capacidad];
        size = capacidad;
    }

    private int hash(String tarjeta) {
        return Math.abs(tarjeta.hashCode()) % size;
    }

    // Insertar con manejo de colisión
    public void insertar(String tarjeta, String usuario) {
        int index = hash(tarjeta);
        int originalIndex = index;
        int contador = 0;

        while (tabla[index] != null) {
            // Si la tarjeta ya existe, actualizamos
            if (tabla[index].getTarjeta().equals(tarjeta)) {
                tabla[index] = new ClosedHashEntry(tarjeta, usuario);
                return;
            }
            index = (index + 1) % size;
            contador++;

            if (contador >= size) {
                throw new RuntimeException("La tabla hash está llena.");
            }
        }
        tabla[index] = new ClosedHashEntry(tarjeta, usuario);
    }

    // Buscar usuario por tarjeta
    public String buscar(String tarjeta) {
        int index = hash(tarjeta);
        int contador = 0;

        while (tabla[index] != null) {
            if (tabla[index].getTarjeta().equals(tarjeta)) {
                return tabla[index].getUsuario();
            }
            index = (index + 1) % size;
            contador++;

            if (contador >= size) {
                break;
            }
        }
        return "No encontrado";
    }

    // Mostrar toda la tabla
    public String mostrarTodo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (tabla[i] != null) {
                sb.append("Posición ").append(i)
                  .append(": Tarjeta ").append(tabla[i].getTarjeta())
                  .append(" → Usuario ").append(tabla[i].getUsuario()).append("\n");
            } else {
                sb.append("Posición ").append(i).append(": [vacío]\n");
            }
        }
        return sb.toString();
    }
}
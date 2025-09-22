public class BusquedaSaltos {
    public static int BusquedaSaltos(int[] arr, int key) {
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n)); // Tamaño del salto
        int prev = 0;
        // Saltar bloques hasta pasarnos del key
        while (arr[Math.min(step, n) - 1] < key) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n) {
                return -1;
            }
        }
        // Búsqueda lineal en el bloque
        for (int i = prev; i < Math.min(step, n); i++) {
            if (arr[i] == key) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        int[] data = {7, 11, 12, 25, 34, 90}; // Debe estar ordenado
        int key = 25;
        int result = BusquedaSaltos(data, key);
        if (result == -1) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento encontrado en la posición: " + result);
        }
    }
}

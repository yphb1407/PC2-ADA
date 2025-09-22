public class BusquedaInterpolacion { 
        public static int BusquedaInterpolacion(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high && key >= arr[low] && key <= arr[high]) {
            // Estimación de la posición usando la fórmula
            int pos = low + ((key - arr[low]) * (high - low)) / (arr[high] - arr[low]);

            if (arr[pos] == key) {
                return pos; // Encontrado
            } else if (arr[pos] < key) {
                low = pos + 1; // Buscar en la mitad derecha
            } else {
                high = pos - 1; // Buscar en la mitad izquierda
            }
        }
        return -1; // No encontrado
    }
    public static void main(String[] args) {
        int[] data = {10, 20, 30, 40, 50, 60, 70}; // Debe estar ordenado y distribución uniforme
        int key = 50;
        int result = BusquedaInterpolacion(data, key);
        if (result == -1) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento encontrado en la posición: " + result);
        }
    }
}

//Mejor que la binaria cuando los datos están uniformemente distribuidos
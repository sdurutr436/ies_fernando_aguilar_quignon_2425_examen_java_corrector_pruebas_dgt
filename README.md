# Corrector de Tests - Paquete dgt.corrector

✅ **Proyecto**: Corrector de tests tipo DGT (Dirección General de Tráfico)

Este paquete (namespace `dgt.corrector`) contiene una solución simple en Java para procesar y calificar tests de tipo tipo cuestionario: carga las soluciones de un archivo principal (`soluciones.txt`), carga las respuestas de los candidatos desde archivos individuales por test (`<idTest>.txt`) y genera un archivo de puntuaciones (`puntuaciones.txt`).

---

## Contexto y autor
- Ejercicio/examen/práctica del curso 2024/2025 de 1º DAW (Grado Superior).  
- Centro: IES Fernando Aguilar Quignon.  
- Autor: Sergio (este repositorio).  
- Nota: el código puede ser incompleto o contener errores, por lo que se indican notas y mejoras más abajo.  

---

## Estructura del paquete `dgt.corrector`

- `CalculadoraPuntuajes.java`  
  - Clase que calcula la puntuación comparando las respuestas del candidato con las soluciones.

- `Candidato.java`  
  - Modelo para un candidato: `nombre`, `id` (actualmente representa el `idTest`), `puntuaje`, y `respuestas`.

- `DAOException.java`  
  - Excepción personalizada para errores del DAO (I/O principalmente).

- `DireccionTraficoApp.java`  
  - Clase principal con `main()`. Coordina la carga de soluciones, carga de candidatos y guardado de puntuaciones.

- `DireccionTraficoDAO.java`  
  - Implementación de `IDireccionTraficoDAO`: lectura de `soluciones.txt`, lectura de `<idTest>.txt` y escritura de `puntuaciones.txt`.

- `IDireccionTraficoDAO.java`  
  - Interfaz que define operaciones de I/O: `cargarSoluciones()`, `cargarCandidatos(idTest)` y `guardarPuntuajes(List<Candidato>)`.

---

## Formato de archivos de entrada y salida

- `soluciones.txt`: fichero que contiene las soluciones de todos los tests disponibles.  
  - Formato por línea: `idTest;resp1;resp2;resp3;...`  
  - Ejemplo:
    - `test1;A;B;C;D;E;A;B;C;D;E`  

- `<idTest>.txt`: fichero de candidatos por test (un archivo por cada `idTest`).  
  - Formato por línea: `nombre;resp1;resp2;resp3;...`  
  - Nota: actualmente `DireccionTraficoDAO.cargarCandidatos` almacena `idTest` en el campo `id` de `Candidato` (no hay un ID único por candidato).  
  - Ejemplo:
    - `Juan Perez;A;B;C;D;E;A;B;C;D;E`

- `puntuaciones.txt` (salida): fichero con los resultados finales. Cada línea incluye el `nombre`, la `puntuacion`, y el `id` del test (tal y como implementado ahora). Si el candidato tiene puntuación menor a 7, se le añade la etiqueta `suspenso`.

---

## Cómo compilar y ejecutar (Windows PowerShell)

Abre PowerShell y sitúate en la carpeta que contiene la carpeta `corrector` (si trabajas desde el directorio `examenes/dgt`):

# Compilar todas las clases
javac -d out corrector/*.java

# Ejecutar la aplicación
java -cp out dgt.corrector.DireccionTraficoApp
```

Nota: Las clases declaran `package dgt.corrector`. Si prefieres compilar moviendo archivos al árbol de paquetes podrás crear la estructura `dgt/corrector` y compilar desde `dgt`.

---

## Ejemplo de uso

1. Crea `soluciones.txt` con contenido:
```
test1;A;B;C;D;E;A;B;C;D;E
```
2. Crea `test1.txt` con contenido:
```
Juan Perez;A;B;C;D;E;A;B;C;D;E
María López;A;B;C;A;E;A;B;C;D;E
```
3. Ejecuta la aplicación (ver pasos compilación).
4. Revisa `puntuaciones.txt` con los registros y la marca `suspenso` cuando el candidato tenga menos de 7 puntos.

---

## Errores conocidos / Limitaciones / Posibles mejoras ⚠️

He detectado varias áreas que se podrían mejorar o donde el programa puede fallar en su forma actual:

1. Comprobaciones nulas y tamaños en `CalculadoraPuntuajes`:
   - `CalculadoraPuntuajes.calcularPuntuaje` solo comprueba si `soluciones` no es `null`; no comprueba `respuestas` ni que ambos `List` tengan la misma longitud. Esto puede provocar `NullPointerException` o `IndexOutOfBoundsException`.
   - Mejora sugerida: comprobar `respuestas` y recorrer hasta `Math.min(soluciones.size(), respuestas.size())`.

2. `DireccionTraficoApp` reutiliza la misma lista `candidatos` fuera del bucle, lo que hace que los candidatos se acumulen si existen múltiples `idTest`. Cuando `guardarPuntuajes` se llama por cada test se guarda la lista completa (no solo los candidatos del test actual). Sugiero mover la creación de la lista `candidatos` dentro del bucle `for (String idTest : mapaSol.keySet())`.

3. `DireccionTraficoDAO.cargarCandidatos` guarda el `idTest` en el campo `id` del `Candidato`. Si se desea almacenar un ID único por candidato (por ejemplo `DNI` o similar), habría que modificar el formato de los archivos `idTest.txt` a `nombre;id;resp1;...` y la lectura correspondiente.

4. `DireccionTraficoDAO.guardarPuntuajes` abre `puntuaciones.txt` con `FileWriter(file_puntuaciones)` que sobrescribe el archivo cada vez. Esto implica que si `guardarPuntuajes` se llama varias veces (por ejemplo por cada test), solo se conserva la última escritura. Para escribir todos los resultados, usar `new FileWriter(file_puntuaciones, true)` (append) o reescribir la lógica para generar el archivo completo de una sola vez.

5. `DireccionTraficoDAO.guardarPuntuajes` recalcula las soluciones al invocar `cargarSoluciones()` nuevamente dentro del método — esto es un coste adicional, pero no crítico para ficheros pequeños.

6. Manejo de excepciones: el código a veces captura `RuntimeException` en lugar de `IOException` o excepciones más concretas. Se sugiere capturar y manejar las excepciones específicas para dar mensajes más claros.

7. Formato de `toString()` en `Candidato`: la salida tiene una mezcla de `format` y `	`. Podría normalizarse para facilitar lectura y parseo automático.

8. Falta de pruebas unitarias: el proyecto no contiene tests. Recomendado: añadir tests JUnit que cubran `CalculadoraPuntuajes`, `DireccionTraficoDAO` con archivos de muestra, y `DireccionTraficoApp` con un DAO mock.

9. Inconsistencia de estructura de directorios: los archivos fuente están en un directorio `corrector/` pero declaran `package dgt.corrector`. El código compila si se usa `javac -d out corrector/*.java`, pero organizar los archivos en la estructura de paquetes (`dgt/corrector`) sería más estándar.

---

## Código sugerido (ejemplo de mejora para la calculadora)

```java
// CalculadoraPuntuajes mejorada
public class CalculadoraPuntuajes {
    public int calcularPuntuaje(List<String> soluciones, List<String> respuestas) {
        if (soluciones == null || respuestas == null) return 0;
        int total = 0;
        int length = Math.min(soluciones.size(), respuestas.size());
        for (int i = 0; i < length; i++) {
            if (soluciones.get(i).equals(respuestas.get(i))) {
                total++;
            }
        }
        return total;
    }
}
```

---

## Tests manuales / Small test file

Crea los ficheros de prueba mencionados en la sección Ejemplo y verifica los contenidos de `puntuaciones.txt`. 

Sugerencia para pruebas unitarias futuras:
- `CalculadoraPuntuajes` -> Tests con listas de distinta longitud, `null`, listas vacías.
- `DireccionTraficoDAO` -> test con ficheros temporales (crear ficheros de ejemplo y leer/escribir con `java.nio.file.Files.createTempFile(...)`).

---

## Licencia y uso
- Repositorio con fines educativos y prácticos. No se garantiza que sea robusto ni apto para un entorno de producción.  
- Autor: Sergio — 1º DAW (IES Fernando Aguilar Quignon) — año 2024/2025.

---

## ¿Quieres que mejore el proyecto?
- Puedo refactorizar y arreglar los errores detectados (por ejemplo, corregir `CalculadoraPuntuajes`, cambiar `DireccionTraficoApp` para procesar cada test de forma independiente, o añadir tests unitarios y una carpeta `resources` con ficheros de ejemplo).
- Indícame qué cambio prefieres (o si quiero que haga todo) y lo implemento.

💡 ¡Con gusto te ayudo a dejarlo listo para evaluación o como un ejemplo completo para el curso!

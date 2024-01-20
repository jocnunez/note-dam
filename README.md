# Note DAM
NoteDAM es una aplicación de gestión de ToDos

## Práctica 1

En esta práctica se busca trabajar con el manejo de ficheros, en este caso en una aplicación de Android hecha con Kotlin.
Para ello usaremos ficheros de tipo CSV, JSON (y opcionalmente XML).

### Obligatorio
 - Se guardarán en fichero(s) CSV un listado de Categorías que el usuario podrá gestionar y que estarán asociados a su vez a una lista de tareas. Se deberá valorar el número de ficheros que se van a usar y argumentar por qué se ha optado por esa opción.
 - Lo mismo pero en fichero(s) JSON

#### Sección de categorías:
En esta vista saldrá un listado (o una representación de alguna forma) de las categorías que tiene creadas el usuario (no se hace gestión de usuarios por ahora)
##### Modelo:
 - Una Categoría debe incluir al menos el nombre y la fecha de creación
 - Además necesitaremos alguna forma de saber cual es la categoría seleccionada
 - En el futuro se incluirá la posibilidad de ordenar las categorías (tened esto en cuenta para el modelo de datos)
##### Acciones:
 - Necesitaremos básicamente un CRUD para las categorías (El Update para indiar cual es la categoría seleccionada, por ejemplo)
 - En las vistas lo único obligatorio es capturar las acciones posibles del usuario:
   - Añadir, Eliminar y Seleccionar

#### Sección de Settings:
En esta sección no hay que hacer nada por ahora. Lo único a tener en cuenta es:
 - Desde el main activity debemos poder seleccionar CSV o JSON (hardcodeado)
 - Además, si hay ya alguna categoría seleccionada se irá directamente a la vista de ToDos asociados a esa categoría

#### Sección de ToDos:
En esta vista se presentará la lista de tareas asociada a la categoría seleccionada
##### Modelo:
Se necesitarán al menos campos para saber:
 - Si es una tarea completada, o no
 - La fecha de creación
 - Y el valor principal será una de estas cuatro opciones:
   - Texto (no admitirá el carácter , para evitar conflictos en el csv)
   - Imagen (la url)
   - Audio (la url)
   - Sublista. Los items de esta sublista solo tendrán:
     - Campo de texto (sin ,)
     - Campo de completado o no

##### Acciones:
Al igual que para las categorías, necesitaremos un crud para poder añadir, eliminar o actualizar tareas.


### Opcional
 - Testing
 - Guardar también en XML como tercera opción de tipo de fichero
 - Permitir comas en los textos y gestionar las consecuencias en CSV
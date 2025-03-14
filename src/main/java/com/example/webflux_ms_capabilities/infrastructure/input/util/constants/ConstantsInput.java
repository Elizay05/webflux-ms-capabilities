package com.example.webflux_ms_capabilities.infrastructure.input.util.constants;

public class ConstantsInput {
    // Paths
    public static final String PATH_CAPABILITIES = "/capabilities";
    public static final String PATH_CAPABILITIES_BY_IDS = "/capabilities/by-ids";

    // Métodos
    public static final String METHOD_CREATE = "createCapability";
    public static final String METHOD_GET = "getCapabilities";
    public static final String METHOD_GET_BY_IDS = "getCapabilitiesByIds";

    // Operaciones
    public static final String OP_CREATE_CAPABILITY = "createCapability";
    public static final String OP_GET_CAPABILITIES = "getCapabilities";
    public static final String OP_GET_CAPABILITIES_BY_IDS = "getCapabilitiesByIds";

    // Resumen y descripciones
    public static final String SUMMARY_CREATE_CAPABILITY = "Crear una capacidad";
    public static final String DESC_CREATE_CAPABILITY = "Registra una nueva capacidad en el sistema con nombre, descripción y hasta 20 tecnologías";
    public static final String SUMMARY_GET_CAPABILITIES = "Obtener capacidades";
    public static final String DESC_GET_CAPABILITIES = "Lista todas las capacidades disponibles con paginación y ordenamiento";
    public static final String SUMMARY_GET_CAPABILITIES_BY_IDS = "Obtener capacidades por IDs";
    public static final String DESC_GET_CAPABILITIES_BY_IDS = "Devuelve una lista de capacidades basadas en una lista de IDs proporcionada";

    // Parámetros de consulta
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_ASC = "asc";
    public static final String PARAM_SORT_BY = "sortBy";

    public static final String DESC_PAGE = "Número de la página (por defecto 0)";
    public static final String DESC_SIZE = "Cantidad de capacidades por página (por defecto 10)";
    public static final String DESC_ASC = "Orden ascendente (true) o descendente (false)";
    public static final String DESC_SORT_BY = "Campo por el cual ordenar (ejemplo: 'name' o 'technologyCount')";

    public static final String EXAMPLE_PAGE = "0";
    public static final String EXAMPLE_SIZE = "10";
    public static final String EXAMPLE_ASC = "true";
    public static final String EXAMPLE_SORT_BY = "name";

    // Códigos de respuesta
    public static final String CODE_200 = "200";
    public static final String CODE_201 = "201";
    public static final String CODE_400 = "400";
    public static final String CODE_404 = "404";
    public static final String CODE_409 = "409";
    public static final String CODE_500 = "500";

    // Mensajes de respuesta
    public static final String RESP_CAPABILITIES_LIST = "Lista de capacidades disponibles";
    public static final String RESP_CAPABILITY_CREATED = "Capacidad creada exitosamente";
    public static final String RESP_ERROR_VALIDATION = "Error en la validación de datos (campo vacío, más de 20 tecnologías, etc.)";
    public static final String RESP_ERROR_EXISTS = "La capacidad ya existe";
    public static final String RESP_ERROR_SERVER = "Error interno del servidor";
    public static final String RESP_BAD_REQUEST = "Solicitud incorrecta debido a parámetros inválidos";
    public static final String RESP_CAPABILITIES_LIST_BY_IDS = "Lista de capacidades obtenidas por IDs";
    public static final String RESP_ERROR_NOT_FOUND = "Algunas o todas las capacidades no fueron encontradas";

    // Ejemplos JSON
    public static final String EXAMPLE_NAME_CREATE = "Ejemplo de creación de capacidad";
    public static final String EXAMPLE_CAPABILITY_CREATE = "{ \"name\": \"Java/Spring\", \"description\": \"Ejemplo de Java/Spring\", \"technologies\": [1, 2, 3] }";
    public static final String EXAMPLE_ERROR_VALIDATION = "{ \"error\": \"name: es requerido, technologies: no puede superar los 20 elementos\" }";
    public static final String EXAMPLE_ERROR_EXISTS = "{ \"error\": \"La capacidad ya existe\" }";
    public static final String EXAMPLE_NAME_GET_BY_IDS = "Ejemplo de obtención de capacidades por IDs";
    public static final String EXAMPLE_CAPABILITY_GET_BY_IDS = "{ \"capabilityIds\": [1, 2, 3] }";
    public static final String EXAMPLE_ERROR_NOT_FOUND = "{ \"error\": \"Algunas capacidades no fueron encontradas\" }";

    public static final String ERROR = "error";
}

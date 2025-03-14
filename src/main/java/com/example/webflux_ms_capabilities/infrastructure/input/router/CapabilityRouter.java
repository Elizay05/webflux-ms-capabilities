package com.example.webflux_ms_capabilities.infrastructure.input.router;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityIdsRequest;
import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityPageResponse;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import com.example.webflux_ms_capabilities.infrastructure.input.handler.CapabilityHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import static com.example.webflux_ms_capabilities.infrastructure.input.util.constants.ConstantsInput.*;

@Configuration
public class CapabilityRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = PATH_CAPABILITIES,
                    beanClass = CapabilityHandler.class,
                    beanMethod = METHOD_CREATE,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = OP_CREATE_CAPABILITY,
                            summary = SUMMARY_CREATE_CAPABILITY,
                            description = DESC_CREATE_CAPABILITY,
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CapabilityRequest.class),
                                            examples = @ExampleObject(
                                                    name = EXAMPLE_NAME_CREATE,
                                                    value = EXAMPLE_CAPABILITY_CREATE
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = CODE_201,
                                            description = RESP_CAPABILITY_CREATED
                                    ),
                                    @ApiResponse(responseCode = CODE_400,
                                            description = RESP_ERROR_VALIDATION,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = EXAMPLE_ERROR_VALIDATION
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_409,
                                            description = RESP_ERROR_EXISTS,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = EXAMPLE_ERROR_EXISTS
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_500,
                                            description = RESP_ERROR_SERVER
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = PATH_CAPABILITIES,
                    beanClass = CapabilityHandler.class,
                    beanMethod = METHOD_GET,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = OP_GET_CAPABILITIES,
                            summary = SUMMARY_GET_CAPABILITIES,
                            description = DESC_GET_CAPABILITIES,
                            parameters = {
                                    @Parameter(name = PARAM_PAGE, description = DESC_PAGE, example = EXAMPLE_PAGE, in = ParameterIn.QUERY),
                                    @Parameter(name = PARAM_SIZE, description = DESC_SIZE, example = EXAMPLE_SIZE, in = ParameterIn.QUERY),
                                    @Parameter(name = PARAM_ASC, description = DESC_ASC, example = EXAMPLE_ASC, in = ParameterIn.QUERY),
                                    @Parameter(name = PARAM_SORT_BY, description = DESC_SORT_BY, example = EXAMPLE_SORT_BY, in = ParameterIn.QUERY)
                            },
                            responses = {
                                    @ApiResponse(responseCode = CODE_200,
                                            description = RESP_CAPABILITIES_LIST,
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = CapabilityPageResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_400,
                                            description = RESP_BAD_REQUEST,
                                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = PATH_CAPABILITIES_BY_IDS,
                    beanClass = CapabilityHandler.class,
                    beanMethod = METHOD_GET_BY_IDS,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = OP_GET_CAPABILITIES_BY_IDS,
                            summary = SUMMARY_GET_CAPABILITIES_BY_IDS,
                            description = DESC_GET_CAPABILITIES_BY_IDS,
                            requestBody = @RequestBody(
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CapabilityIdsRequest.class),
                                            examples = @ExampleObject(
                                                    name = EXAMPLE_NAME_GET_BY_IDS,
                                                    value = EXAMPLE_CAPABILITY_GET_BY_IDS
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = CODE_200,
                                            description = RESP_CAPABILITIES_LIST_BY_IDS,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = CapabilityResponse.class))
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_400,
                                            description = RESP_BAD_REQUEST,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_404,
                                            description = RESP_ERROR_NOT_FOUND,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    examples = @ExampleObject(
                                                            value = EXAMPLE_ERROR_NOT_FOUND
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = CODE_500,
                                            description = RESP_ERROR_SERVER
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> capabilityRoutes(CapabilityHandler handler) {
        return RouterFunctions.route()
                .POST(PATH_CAPABILITIES, handler::createCapability)
                .GET(PATH_CAPABILITIES, handler::getCapabilities)
                .POST(PATH_CAPABILITIES_BY_IDS, handler::getCapabilitiesByIds)
                .build();
    }
}

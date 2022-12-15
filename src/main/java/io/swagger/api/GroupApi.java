package io.swagger.api;

import io.swagger.model.AccessGroup;
import io.swagger.model.Mail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Validated
public interface GroupApi {
    @Operation(summary = "Creates access group",security = {
            @SecurityRequirement(name = "bearerAuth")    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/group",
            method = RequestMethod.POST)
    AccessGroup createGroup(@RequestParam(value = "groupName") String groupName,
                            @RequestParam(value = "usernames") Set<String> usernames,
                            @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Gets access group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/group",
            method = RequestMethod.GET)
    AccessGroup getGroup(@RequestParam(value = "groupName") String groupName,
                         @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Delete access group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/group",
            method = RequestMethod.DELETE)
    void deleteGroup(@RequestParam(value = "groupName") String groupName,
                            @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Edit access group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/group",
            method = RequestMethod.PATCH)
    AccessGroup editGroup(@RequestParam(value = "groupName") String groupName,
                            @RequestParam(value = "usernames") Set<String> usernames,
                            @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

}

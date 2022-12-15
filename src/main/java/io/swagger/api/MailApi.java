package io.swagger.api;

import io.swagger.model.Credential;
import io.swagger.model.Mail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Validated
public interface MailApi {
    @Operation(summary = "Reads all mails", description = "Triggers read all operation from INBOX")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/trigerReadMails",
            method = RequestMethod.GET)
    void triggerReadMails(@Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Reads all user's mails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail/all",
            method = RequestMethod.GET)
    List<Mail> getSavedMails(@Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Reads the latest user's mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail/latest",
            method = RequestMethod.GET)
    Mail getLatestMail(@Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Reads user's mails by subject and sent date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail",
            method = RequestMethod.GET)
    List<Mail> getMailsBySubjectAndDate(@RequestParam(value = "subject") String subject,
                                        @RequestParam(value = "sentDate", required = false) Date sentDate,
                                        @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Reads user's mail by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail/{id}",
            method = RequestMethod.GET)
    Mail getMailById(@PathVariable(value = "id") Long id, @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Deletes user's mails by ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail",
            method = RequestMethod.DELETE)
    void deleteMailsByIds(@RequestParam(value = "ids") Set<Long> ids, @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Adds access groups to a mails by ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail/groups",
            method = RequestMethod.POST)
    List<Mail> addGroupsForMailsByIds(@RequestParam(value = "groups") Set<String> groups,
                             @RequestParam(value = "ids") Set<Long> ids, @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);

    @Operation(summary = "Deletes access groups for a mail by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")})
    @RequestMapping(value = "/mail/group",
            method = RequestMethod.GET)
    Mail deleteGroupsForMailById(@RequestParam(value = "groupName") Set<String> groupNames,
                                 @RequestParam(value = "id") Long id,
                                 @Parameter(in = ParameterIn.DEFAULT) HttpServletResponse response);
}

package ir.htsc.rest.endpoint;

import ir.htsc.rest.service.TotpService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author me-sharifi on 6/10/2019 at 11:57 AM.
 */
@Path("hotp")
public class HOTPEndpoint {
    @Context
    private UriInfo uriInfo;
    @Context
    private HttpServletRequest request;

    @Inject
    private TotpService service;

    @GET
    public Response sayHello() {
        return Response.ok("Hello").build();
    }
}

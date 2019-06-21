package ir.htsc.rest.endpoint;

import ir.htsc.rest.service.TotpService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by MSH on 5/26/2019.
 */
@Path("ir/htsc/cache")
public class TotpCacheEndpoint {

    @Resource(mappedName = "totp_key_cache")//Correct
     private com.tangosol.net.NamedCache cache;
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

//    @GET
//    @Path("sms/generate/mobiles/{mobile-no}/cards/{card-no}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response put(@PathParam("mobile-no") String mobileNo,
//                                     @PathParam("card-no") String cardNo) {
//        boolean result = false;
//        OTPKeyStoreCache totpKeyStoreCache= new OTPKeyStoreCache(mobileNo,cardNo);
//        cache.put(cardNo+mobileNo,totpKeyStoreCache);
//        return Response.ok(JSONFormatter.toJSON(cache.get(cardNo+mobileNo))).build();
//    }

}

package ir.htsc.rest.endpoint;

import ir.htsc.AppConstants;
import ir.htsc.entity.Provision;
import ir.htsc.log.Loggable;
import ir.htsc.rest.dto.OTPResponseDto;
import ir.htsc.rest.service.TotpService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;

/**
 * @author me-sharifi on 5/26/2019 at 10:50 AM.
 */
@Path("totp")
@Loggable
public class TOTPEndpoint {

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

    @GET
    @Path("provision/mobiles/{mobile-no}/cards/{card-no}/for/{otp-for}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response provision(@PathParam("mobile-no") @NotNull(message = "#mobile No cant be null!") String mobileNo,
                              @PathParam("card-no") @NotNull(message = "#card No cant be null!") String cardNo,
                              @PathParam("otp-for") @NotNull(message = "# otpFor cant be null") String otpForStr,
                              @HeaderParam("Password") String otpStaticPassword
    ) throws Exception {
        Provision provision = service.provision(cardNo, mobileNo, otpForStr, otpStaticPassword);
        AppConstants.printErr(provision + "");
        OTPResponseDto dto = new OTPResponseDto();
        dto.setActivationKey1(provision.getAesKeyPart1());
        dto.setExpireAt(provision.getOtpEmbedded().getExpireAt());
        dto.setLength(provision.getOtpEmbedded().getOtpLength());
        dto.setMobileNo(provision.getOtpEmbedded().getMobileNo());
        dto.setCardNo(provision.getOtpEmbedded().getCardNoSkipMasked());
        dto.setUsedFor(provision.getOtpEmbedded().getUsedFor());
        dto.setStep(provision.getOtpEmbedded().getStep());
        return Response.ok(dto.toJSON()).build();
    }

    @GET
    @Path("active/mobiles/{mobile-no}/cards/{card-no}/for/{otp-for}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response active(@PathParam("mobile-no") String mobileNo,
                           @PathParam("card-no") String cardNo,
                           @PathParam("otp-for") @NotNull(message = "# otpFor cant be null") String otpForStr,//01 for first pass , 02 for second pass
                           @HeaderParam("Activation-Code") String activationCode)
            throws Exception {
        long start = System.currentTimeMillis();
        Provision provision = service.active(cardNo, mobileNo, otpForStr, activationCode);
        AppConstants.printErr("#time " + (System.currentTimeMillis() - start) + " ms ,result: " + provision);
        if (provision != null && !provision.getEnabled()) {
            OTPResponseDto dto = new OTPResponseDto();
            dto.setKey(provision.getOtpEncryptedKeyPart2());
            dto.setLength(provision.getOtpEmbedded().getOtpLength());
            dto.setAlgorithm(provision.getOtpEmbedded().getAlgorithm());
            dto.setExpireAt(provision.getOtpEmbedded().getExpireAt());
            dto.setMobileNo(provision.getOtpEmbedded().getMobileNo());
            dto.setCardNo(provision.getOtpEmbedded().getCardNoSkipMasked());
            dto.setUsedFor(provision.getOtpEmbedded().getUsedFor());
            dto.setStep(provision.getOtpEmbedded().getStep());
            return Response.ok(dto.toJSON()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Not found").build();
    }


    @GET
    @Path("generate/mobiles/{mobile-no}/cards/{card-no}/for/{otp-for}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response generate(@PathParam("mobile-no") String mobileNo,
                             @PathParam("card-no") String cardNo,
                             @PathParam("otp-for") @NotNull(message = "# otpFor cant be null") String otpForStr) {//01 for first pass , 02 for second pass
        System.out.println("mobileNo = [" + mobileNo + "], cardNo = [" + cardNo + "], otpForStr = [" + otpForStr + "]");
        BigDecimal bigInteger = new BigDecimal(mobileNo + cardNo + otpForStr);
        String result = service.generate(bigInteger);
        return Response.ok(result).build();
    }

    @GET
    @Path("validate/mobiles/{mobile-no}/cards/{card-no}/for/{otp-for}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response validate(@PathParam("mobile-no") String mobileNo,
                             @PathParam("card-no") String cardNo,
                             @PathParam("otp-for") @NotNull(message = "# otpFor cant be null") String otpForStr,//01 for first pass , 02 for second pass
                             @HeaderParam("OTP") String totp,
                             @HeaderParam("Password") String staticPassword,
                             @HeaderParam("Check-Last-OTP") boolean lastOTP) {
        boolean result = service.validate(new BigDecimal(mobileNo + cardNo + otpForStr), totp,  staticPassword,lastOTP);
        return Response.ok(result).build();
    }

    //    @GET
//    @Path("register/mobiles/{mobile-no}/cards/{card-no}/for/{otp-for}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response register(@PathParam("mobile-no") String mobileNo,
//                             @PathParam("card-no") String cardNo,
//                             @PathParam("otp-for") @NotNull(message = "# otpFor cant be null") String otpForStr) {
//        long start = System.currentTimeMillis();
//        OTPKeyStore result = service.register(cardNo, mobileNo, otpForStr);
//        AppConstants.print("#time " + (System.currentTimeMillis() - start) + " ms ,result: " + result);
//        return Response.ok(JSONFormatter.toJSON(result)).build();
//    }
}

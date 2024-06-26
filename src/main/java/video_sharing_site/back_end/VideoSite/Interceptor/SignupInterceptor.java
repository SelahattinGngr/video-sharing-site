package video_sharing_site.back_end.VideoSite.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Dto.UserDTO;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserPasswordValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameValidateException;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;
import video_sharing_site.back_end.VideoSite.Shared.Services.Validations.SignupValidationService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Component
public class SignupInterceptor implements HandlerInterceptor {

    @Autowired
    private SignupValidationService validationService;

    @Autowired
    private LogConfig logConfig;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request.getInputStream());
        ResponseEntity<Map<String, Object>> errorResponse;
        try {

            String email = jsonNode.get("email").asText();
            String username = jsonNode.get("userName").asText();
            String password = jsonNode.get("password").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            LocalDate birthday = LocalDate.parse(jsonNode.get("birthday").asText());

            validationService.validateEmail(email);
            validationService.validateUsername(username);
            validationService.validatePassword(password);

            UserDTO user = new UserDTO(null, false, firstName, lastName, email, username, password, birthday, null);
            request.setAttribute("userDto", user);
            return true;
        } catch (UserEmailException e) {
            errorResponse = new BaseUserExceptions().emailException();
        } catch (UserUsernameException e) {
            errorResponse = new BaseUserExceptions().usernameException();
        } catch (UserInvalidException e) {
            errorResponse = new BaseUserExceptions().invalidException();
        } catch (UserEmailValidateException e) {
            errorResponse = new BaseUserExceptions().emailValidateException();
        } catch (UserUsernameValidateException e) {
            errorResponse = new BaseUserExceptions().usernameValidateException();
        } catch (UserPasswordValidateException e) {
            errorResponse = new BaseUserExceptions().passwordValidateException();
        }
        handleException(errorResponse, response);
        logConfig.log(request.getMethod(), getClass().getName(), errorResponse.getBody().get("Error").toString(), null);
        return false;
    }

    private void handleException(ResponseEntity<Map<String, Object>> errorResponse, HttpServletResponse response)
            throws IOException {
        response.setStatus(errorResponse.getStatusCode().value());
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse.getBody());
        response.getWriter().write(json);
    }
}

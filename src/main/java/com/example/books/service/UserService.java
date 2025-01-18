package com.example.books.service;

import com.example.books.controllers.UserController;
import com.example.books.exceptions.BusinessException;
import com.example.books.exceptions.DuplicateException;
import com.example.books.models.Role;
import com.example.books.models.SignUp;
import com.example.books.models.User;
import com.example.books.repos.AuthorRepo;
import com.example.books.repos.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.bytebuddy.utility.RandomString;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final String REFRESH_TOKEN_DOES_NOT_EXIST = "refresh token does not exist";
    @Autowired
    private UserRepo repo;
    private final Logger LOGGER= LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username){
        LOGGER.info("inside exists by username");
        return repo.existsByUsername(username);
    }

    public boolean existsByEmail(String email){return repo.existsByEmail(email);}
    public User getByUsername(String username){
        LOGGER.info("inside get by username");
        return repo.findByUsername(username).get();
    }

    public String hashPassword(String passwordToHash){
        String generatedPassword = null;

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void createUser(User user){
        repo.save(user);
    }

    public void updateUser(User user){
        repo.save(user);
    }

    public void savePassword(User user,String password){
        user.setPassword(hashPassword(password));
        repo.save(user);
    }

    public boolean verifyUser(String code){
        Optional<User> userO= repo.findByVerifiedCode(code);
        if(userO.isPresent()){
            User user=userO.get();
            user.setEnabled(true);
            repo.save(user);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public void signup(SignUp request) {
        String username = request.getUsername();
        Optional<User> existingUser = repo.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new DuplicateException(String.format("User with the same username '%s' already exists.", username));
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user=new User(request.getFirstname(),request.getLastname(),request.getUsername(),hashedPassword,request.getEmail(),false, RandomString.make(64), Role.USER);

        repo.save(user);
    }

  /*  @Transactional // Ensures that the logout process, including any database changes, is handled within a single transaction
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {

        // Retrieves the current authentication information from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Checks if the user is authenticated (authentication is not null and authenticated)
        if (authentication != null && authentication.isAuthenticated()) {

            // Logs out the user by clearing their authentication info from the SecurityContext
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            // Gets the Authorization header from the request to retrieve the JWT token
            String token = request.getHeader("Authorization");

            // Checks if the token is in "Bearer <token>" format, then removes "Bearer " prefix to extract only the token value
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Extracts the actual token by removing the "Bearer " prefix
            } else {
                // If no valid token is provided, throws a custom exception indicating the refresh token does not exist
                throw new BusinessException( REFRESH_TOKEN_DOES_NOT_EXIST);
            }

            // Searches the database for the provided token in the refreshTokenRepository
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                    // If no matching token is found, throws an exception for a non-existing refresh token
                    .orElseThrow(() -> new BusinessException(ERROR, REFRESH_TOKEN_DOES_NOT_EXIST));

            // Sets the status of the found refresh token to REVOKED, marking it as unusable
            refreshToken.setRefreshTokenStatus(RefreshTokenStatus.REVOKED);

            // Saves the updated refresh token back to the database to persist the status change
            refreshTokenRepository.save(refreshToken);

            // Returns a successful response indicating that the logout process was completed
            return ResponseEntity.ok(ApiResponse.success(true));
        } else {
            // Throws an exception if the user was not authenticated in the first place
            throw new BusinessException(ERROR, USER_DOES_NOT_EXIST);
        }
    } */
}

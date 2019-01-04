package com.knowwhere.notshazamserver.user.services;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.user.repos.BaseUserRepo;
import com.knowwhere.notshazamserver.utils.NoSuchResourceException;
import com.knowwhere.notshazamserver.utils.ResourceAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class BaseUserService {
    @Autowired
    private BaseUserRepo baseUserRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * This method persists and returns a BaseUser instance. If the passed in params already define a user a RuntimeException(ResourceAlreadyExistsException) is thrown
     * @param baseUser- -> The BaseUser to be persisted
     * @return BaseUser
     */
    public BaseUser createBaseUser(BaseUser baseUser) {
        BaseUser emailUser = this.baseUserRepo.findByEmailOrPhone(baseUser.getEmail(), baseUser.getPhone());
        if( emailUser != null )
            throw new ResourceAlreadyExistsException("BaseUser", "email/phone", baseUser.getEmail()+"/"+baseUser.getPhone());

        return this.baseUserRepo.save(baseUser);
    }

    /**
     * A helper method that creates a user for an organization
     * @param orgEmail- -> The email of the organization
     * @param orgPhone- -> The phone number of the organization
     * @return BaseUser
     */
    public BaseUser createOrganizationUser(String orgEmail, String orgPhone) {
        BaseUser adminUser = new BaseUser(orgEmail, orgPhone, orgPhone);
        return this.createBaseUser(adminUser);
    }

    /**
     * Returns a BaseUser instance with the provided id
     * @param id- -> The id to hit the database with
     * @return BaseUser
     */
    public BaseUser getBaseUserById(Long id){
        return this.baseUserRepo.findById(id).orElseThrow(() -> new NoSuchResourceException("BaseUser", "id", id.toString()));
    }

    /**
     * This method returns a BaseUser given the correct email and password combo
     * @param email- -> The email of the user
     * @param password- -> The password of the user
     * @return BaseUser
     */
    public BaseUser getBaseUserByEmailAndPassword(String email, String password){
        return this.baseUserRepo.findByEmailAndPassword(email, password).orElseThrow(()-> new RuntimeException("Wrong email password combo"));
    }
    /**
     * This method returns a BaseUser given the correct phone and password combo
     * @param phone- -> The phone of the user
     * @param password- -> The password of the user
     * @return BaseUser
     */
    public BaseUser getBaseUserByPhoneAndPassword(String phone, String password){
        return this.baseUserRepo.findByPhoneAndPassword(phone, password).orElseThrow(()-> new RuntimeException("Wrong phone password combo"));
    }




}

package com.loan_manager_app.loans_manager.AUTH.mapper;

import com.loan_manager_app.loans_manager.AUTH.events.CurrentUserResponse;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CurrentUserMapper {
    public static CurrentUserResponse map(Users user){
        return new CurrentUserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getSurname() + " " + user.getOtherNames(),
                user.getRole().toString(),
                true
        );

    }
}

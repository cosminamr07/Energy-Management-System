package user_service.repositories;

import org.springframework.data.repository.CrudRepository;
import user_service.entities.UserInfo;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserInfo, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    UserInfo findUserInfoByEmail(String email);
 //   User findByIdUser(UUID idUser);

}

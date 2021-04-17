package zup.postblog.springbootapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import zup.postblog.springbootapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
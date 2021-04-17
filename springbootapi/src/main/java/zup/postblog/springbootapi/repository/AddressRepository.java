package zup.postblog.springbootapi.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import zup.postblog.springbootapi.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    public List<Address> findByUserId(Long postId);

}
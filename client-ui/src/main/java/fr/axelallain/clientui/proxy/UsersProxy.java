package fr.axelallain.clientui.proxy;

import fr.axelallain.clientui.model.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zuul")
@RibbonClient(name = "users")
public interface UsersProxy {

    @GetMapping(value = "/users/users/{id}")
    User usersById(@PathVariable("id") int id);

    @GetMapping(value = "/users/users/{email}")
    User usersByEmail(@PathVariable("email") String email);
}

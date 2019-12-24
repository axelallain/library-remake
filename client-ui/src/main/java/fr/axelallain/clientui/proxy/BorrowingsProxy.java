package fr.axelallain.clientui.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "zuul")
@RibbonClient(name = "borrowings")
public interface BorrowingsProxy {
}

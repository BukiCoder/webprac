package web_prak.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web_prak.DAO.*;
import web_prak.filters.OrderFilter;
import web_prak.models.*;
import web_prak.security.ClientUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderDAO orderDAO;
    private final CarDAO carDAO;
    private final ClientDAO clientDAO;
    private final ManagerDAO managerDAO;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public String list(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long carId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDateTo,
            @RequestParam(required = false) Integer testDriveDurationMin,
            @RequestParam(required = false) Integer testDriveDurationMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime testDriveDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime testDriveDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDateTo,
            @RequestParam(required = false) String deliveryAddress,
            @RequestParam(required = false) Order.OrderStatus status,
            @RequestParam(required = false) Boolean myOrders,
            Model model,
            @AuthenticationPrincipal ClientUserDetails currentUser) {

        OrderFilter minFilter = new OrderFilter();
        OrderFilter maxFilter = new OrderFilter();

        minFilter.setClientId(clientId);        maxFilter.setClientId(clientId);
        minFilter.setCarId(carId);              maxFilter.setCarId(carId);
        minFilter.setStatus(status);            maxFilter.setStatus(status);
        minFilter.setDeliveryAddress(deliveryAddress); maxFilter.setDeliveryAddress(deliveryAddress);

        minFilter.setOrderDate(orderDateFrom);
        maxFilter.setOrderDate(orderDateTo);

        minFilter.setTestDriveDuration(testDriveDurationMin);
        maxFilter.setTestDriveDuration(testDriveDurationMax);

        minFilter.setTestDriveDate(testDriveDateFrom);
        maxFilter.setTestDriveDate(testDriveDateTo);

        minFilter.setDeliveryDate(deliveryDateFrom);
        maxFilter.setDeliveryDate(deliveryDateTo);

        if (Boolean.TRUE.equals(myOrders)) {
            Manager currentManager = currentUser.getManager();
            minFilter.setManager(currentManager);
            maxFilter.setManager(currentManager);
        }

        List<Order> orders = orderDAO.searchByFilter(minFilter, maxFilter);
        model.addAttribute("orders", orders);
        model.addAttribute("filter", minFilter);
        return "orders/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(@RequestParam Long carId, Model model) {
        Car car = carDAO.getById(carId);
        model.addAttribute("car", car);
        model.addAttribute("order", new Order());
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            model.addAttribute("clients", clientDAO.getAll());
        }
        return "orders/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam Long carId,
                         @RequestParam(required = false) Long clientId,
                         @RequestParam(required = false) Boolean needTestDrive,
                         @RequestParam(required = false) Integer testDriveDuration,
                         @RequestParam(required = false) String testDriveDate,
                         @RequestParam(required = false) Boolean needDelivery,
                         @RequestParam(required = false) String deliveryDate,
                         @RequestParam(required = false) String deliveryAddress,
                         @AuthenticationPrincipal ClientUserDetails currentUser) {

        Car car = carDAO.getById(carId);
        car.setStatus(Car.CarStatus.ordered);
        carDAO.update(car);
        Order order = new Order();
        order.setCar(car);
        order.setOrderDate(LocalDate.now());

        Client client;
        if (clientId != null) {
            client = clientDAO.getById(clientId);
        } else {
            client = currentUser.getClient();
            if (client == null) {
                throw new IllegalStateException("Не удалось определить клиента");
            }
        }
        order.setClient(client);

        order.setManager(managerDAO.getAll().iterator().next());

        if (needTestDrive != null && needTestDrive) {
            order.setTestDriveDuration(testDriveDuration);
            order.setTestDriveDate(LocalDateTime.parse(testDriveDate));
        }
        if (needDelivery != null && needDelivery) {
            order.setDeliveryAddress(deliveryAddress);
            order.setDeliveryDate(LocalDate.parse(deliveryDate));
        }
        order.setStatus(Order.OrderStatus.in_process);
        orderDAO.save(order);
        return "redirect:/cabinet";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        Order order = orderDAO.getById(id);
        order.setStatus(Order.OrderStatus.canceled);
        order.getCar().setStatus(Car.CarStatus.available);
        carDAO.update(order.getCar());
        orderDAO.update(order);
        return "redirect:/cabinet";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderDAO.getById(id);
        order.setStatus(Order.OrderStatus.valueOf(status));
        orderDAO.update(order);
        return "redirect:/orders";
    }
}
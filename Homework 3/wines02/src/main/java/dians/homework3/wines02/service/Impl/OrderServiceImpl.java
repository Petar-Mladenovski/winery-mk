package dians.homework3.wines02.service.Impl;

import dians.homework3.wines02.dto.OrderDto;
import dians.homework3.wines02.mapper.OrderMapper;
import dians.homework3.wines02.model.*;
import dians.homework3.wines02.repository.OrderRepository;
import dians.homework3.wines02.service.OrderService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static dians.homework3.wines02.mapper.OrderMapper.mapToOrderDto;
import static dians.homework3.wines02.mapper.WineryMapper.mapToWineryDto;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(UserEntity userEntity) {
        Cart cart = userEntity.getCart();
        Order order = new Order();
        order.setOrderWines(cart.getCartWines());
        order.setCreatedBy(userEntity);
        long uniqueNumber = counter.getAndIncrement();
        order.setCode(String.valueOf(uniqueNumber));
        order.setStatus(Status.Preparing);
        orderRepository.save(order);
    }

    @Override
    public OrderDto getById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return mapToOrderDto(order.get());
        }
        try {
            throw new NotFoundException("Order with ID " + orderId + " not found");
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderDto> findByUser(Long userId) {
        return orderRepository.findAll().stream().filter(order -> order.getCreatedBy().getId().equals(userId)).map(OrderMapper::mapToOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto makeOrder(List<AddWines> addWines, UserEntity user, Integer totalPrice) {
        Order order = new Order();
        order.setOrderWines(addWines);
        order.setTotal(totalPrice);
        order.setStatus(Status.Preparing);
//        List<Role> roles = user.getRoles().stream().filter((role) -> role.getName().equals("ROLE_CASHIER")).toList();
//        if(!roles.isEmpty()) {
//            order.setStatus(Status.HERE);
//        } else {
//            order.setStatus(Status.WAITING);
//        }
        order.setCreatedBy(user);
        LocalDateTime date = LocalDateTime.now();
        StringBuilder code = new StringBuilder(date.getSecond() + "" + date.getMinute() + "" + date.getHour() + "" + date.getDayOfMonth() + "" + date.getMonthValue() + "" + date.getYear());
        while(code.length() < 14) {
            code.append("0");
        }
        order.setCode(code.toString());
        orderRepository.save(order);
        return OrderMapper.mapToOrderDto(order);
    }
}
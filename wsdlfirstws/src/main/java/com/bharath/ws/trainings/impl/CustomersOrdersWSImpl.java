package com.bharath.ws.trainings.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bharath.ws.trainings.CreateOrdersRequest;
import com.bharath.ws.trainings.CreateOrdersResponse;
import com.bharath.ws.trainings.CustomerOrdersPortType;
import com.bharath.ws.trainings.DeleteOrdersRequest;
import com.bharath.ws.trainings.DeleteOrdersResponse;
import com.bharath.ws.trainings.GetOrdersRequest;
import com.bharath.ws.trainings.GetOrdersResponse;
import com.bharath.ws.trainings.Order;
import com.bharath.ws.trainings.Product;

public class CustomersOrdersWSImpl implements CustomerOrdersPortType {

	Map<BigInteger, List<Order>> customerOrders = new HashMap<>();
	int currentCustomerId;

	public CustomersOrdersWSImpl() {
		init();
	}

	public void init() {
		List<Order> orders = new ArrayList<Order>();
		Order order = new Order();
		order.setId(BigInteger.valueOf(1));
		Product product = new Product();
		product.setDescription("IPhone");
		product.setId("1");
		product.setQuantity(BigInteger.valueOf(3));

		order.getProduct().add(product);

		orders.add(order);
		customerOrders.put(BigInteger.valueOf(++currentCustomerId), orders);
	}

	@Override
	public GetOrdersResponse getOrders(GetOrdersRequest request) {

		BigInteger customerId = request.getCustomerId();
		List<Order> requestOrders = customerOrders.get(customerId);
		
		GetOrdersResponse response = new GetOrdersResponse();
		List<Order> responseOrders = response.getOrder();
		
		for(Order order : requestOrders) {
			responseOrders.add(order);
		}
		return response;
	}

	@Override
	public CreateOrdersResponse createOrders(CreateOrdersRequest request) {

		Order order = request.getOrder();
		
		List<Order> currentOrder = customerOrders.get(request.getCustomerId());
		currentOrder.add(order);
		
		CreateOrdersResponse createOrdersResponse = new CreateOrdersResponse();
		createOrdersResponse.setResult(true);
		
		return createOrdersResponse;
	}

	@Override
	public DeleteOrdersResponse deleteOrders(DeleteOrdersRequest request) {
		
		Order order = request.getOrder();
		BigInteger customerId = request.getCustomerId();
		DeleteOrdersResponse deleteOrdersResponse = new DeleteOrdersResponse();
		if ( customerId != null && order != null) {
			List<Order> list = customerOrders.get(customerId);
			for(Order order2 : list) {
				if(order2.getId() == order.getId()) {
					list.remove(order);
					deleteOrdersResponse.setResult(true);
					return deleteOrdersResponse;
				}
			}
		}
		deleteOrdersResponse.setResult(false);
		return deleteOrdersResponse;
		
	}
}

package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import ca.jrvs.apps.trading.service.TraderAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Api(value = "Trader", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Controller
@RequestMapping("/trader")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class TraderAccountController {

    private TraderAccountService traderAccountService;

    @Autowired
    public TraderAccountController(TraderAccountService traderAccountService){
        this.traderAccountService = traderAccountService;
    }

    @ApiOperation(value = "Create a trader and an account.",
    notes = "TraderId and AccountId are auto generated by the database,"
    + "and they should be identical. Assume each trader has exactly one account.")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(
            path = "/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@PathVariable String firstname,
                                          @PathVariable String lastname,
                                          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dob,
                                          @PathVariable String country,
                                          @PathVariable String email){

        try{
            Trader trader = new Trader();
            trader.setFirstName(firstname);
            trader.setLastName(lastname);
            trader.setCountry(country);
            trader.setEmail(email);
            trader.setDob(Date.valueOf(dob));
            return traderAccountService.createTraderAndAccount(trader);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Create a trader and an account with DTO",
    notes = "TraderId and AccountId are auto generated by the database, " +
            "and they should be identical. Assume each trader has exact one account.")
    @ResponseBody
    @PostMapping(path = "/", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public TraderAccountView createTrader(@RequestBody Trader trader){
        try{
            return traderAccountService.createTraderAndAccount(trader);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Delete a trader",
    notes = "Delete a trader IFF its account amount is zero and no open positions."
    + " Also delete the associated account and securityOrders.")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to delete the user")})
    @DeleteMapping(path = "/traderId/{traderId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteTrader(@PathVariable Integer traderId){
        try{
            traderAccountService.deleteTraderById(traderId);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Deposit a fund",
    notes = "Deposit a fund to the account that associates with the given traderId. Deposit" +
            " amount must be greater than 0.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "traderId is not found"),
            @ApiResponse(code = 400, message = "Unable to deposit due to user input")})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping(path = "/deposit/traderId/{traderId}/amount/{amount}")
    public Account depositFund(@PathVariable Integer traderId, @PathVariable Double amount){
        try{
            return traderAccountService.deposit(traderId, amount);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Withdraw a fund",
    notes = "Withdraw a fund from the account that associates with the given traderId." +
            " Withdraw amount must not exceed account amount.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "traderId is not found"),
            @ApiResponse(code = 400, message = "Unable to withdraw due to user input.")
    })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping(path = "/withdraw/traderId/{traderId}/amount/{amount}")
    public Account withdrawFund(@PathVariable Integer traderId, @PathVariable Double amount){
        try{
            return traderAccountService.withdraw(traderId, amount);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

}


package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "quote", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Controller
@RequestMapping("/quote")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class QuoteController {

    private QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService){
        this.quoteService = quoteService;
    }

    @ApiOperation(value = "Show iexQuote.", notes = "Show iexQuote for a given ticker/symbol.")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "ticker is not found")})
    @GetMapping(path = "/iex/ticker/{ticker}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public IexQuote getQuote(@PathVariable String ticker){
        try{
            return quoteService.findIexQuoteByTicker(ticker);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Update quote table using IEX data.",
        notes = "Update all quotes in the quote table. Use IEX trading API as market data source.")
    @PutMapping(path = "/iexMarketData")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Quote> updateMarketData(){
        try{
            return quoteService.updateMarketData();
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Update a given quote in the quote table.",
        notes = "Manually update a quote in the quote table using IEX market data.")
    @PutMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Quote putQuote(@RequestBody Quote quote){
        try{
            return quoteService.saveQuote(quote);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Add a new ticker to the dailyList (quoteTable).",
        notes = "Add a new ticker/symbol to the quote table, so trader can trade this security.")
    @PostMapping(path = "/tickerId/{tickerId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Quote createQuote(@PathVariable String tickerId){
        try{
            // refactored with helper function in quoteService
            return quoteService.saveQuoteByTickerId(tickerId);
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }

    @ApiOperation(value = "Show the dailyList.",
            notes = "Show dailyList for this trading system (dailyList = quoteTable).")
    @GetMapping(path = "/dailyList")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Quote> getDailyList(){
        try{
            return (List<Quote>) quoteService.findAllQuotes();
        } catch (Exception ex){
            throw ResponseExceptionUtil.getResponseStatusException(ex);
        }
    }
}
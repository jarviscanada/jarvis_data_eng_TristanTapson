import { Component } from 'react';
// Imports we need for routing, provided in react-router-dom library
import { BrowserRouter, Redirect, Route, Switch } from 'react-router-dom';

// We have to import all components to. beable to use them.
// Currently, we should only have the Dashboard component
import Dashboard from './page/Dashboard';
import TraderAccountPage from './page/TraderAccount';
import QuotePage from './page/Quote';

// Initialization of Router Component
// Every React component extends Component from "react" library
export default class Router extends Component {

    render() {
        return (
            <BrowserRouter>
                <Switch>
                    <Route exact path="/">
                        <Redirect to="/dashboard" />
                    </Route>
                    <Route exact path="/dashboard">
                        <Dashboard />
                    </Route>
                    <Route exact path="/trader/:traderId">
                        <TraderAccountPage />
                    </Route>
                    <Route exact path="/quotes">
                        <QuotePage />
                    </Route>
               </Switch>
            </BrowserRouter>
        )
    }
}
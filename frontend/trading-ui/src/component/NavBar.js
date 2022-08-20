import { Component } from 'react';
import { NavLink } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faAddressBook as dashboardIcon,
    faMoneyBill as quoteIcon // new icon for quote menu item
} from '@fortawesome/free-solid-svg-icons';

import './NavBar.scss';

export default class Navbar extends Component {

    render() {
        return (
            <nav className="page-navigation">
                <NavLink to="/" className="page-navigation-header">
                    <></>
                </NavLink>
                <NavLink to="/dashboard" className="page-navigation-item">
                    <FontAwesomeIcon icon={ dashboardIcon } color="coral"/>
                </NavLink>
                <NavLink to="/quotes" className="page-navigation-item">
                    <FontAwesomeIcon icon={ quoteIcon } color="coral"/>
                </NavLink>
               


            </nav>
        );
    }
}
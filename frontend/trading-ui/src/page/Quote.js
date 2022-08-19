import { Component } from 'react';
import { withRouter } from 'react-router';
import axios from 'axios';


import { dailyListQuotesUrl } from '../util/constants';
import Navbar from '../component/NavBar';
import QuoteList from '../component/QuoteList';

import 'antd/dist/antd.min.css';
import "./Quote.scss";

export default withRouter(class QuotePage extends Component {
	constructor(props) {
        super(props);
        this.state = {
            quotes: []
        };
    }
    async componentDidMount() {
		// Fetch quotes here
        const response = await axios.get(dailyListQuotesUrl);
        if(response){
            this.setState({
                quotes: response.data.map(data => ({...data, key: data.ticker})) || []
            });
        }
    }

    render () {
		// Render the quotes page, similar to Dashboard
        return(
            <div className="quote-page">
                <Navbar />
                <div className="quote-page-content">
                    <div className="title">
                        Quotes
                    </div>
                    <QuoteList quotes={ this.state.quotes} />
                </div>
            </div>
        )
    }
});
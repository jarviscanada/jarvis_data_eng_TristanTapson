import { Component } from 'react';
import { withRouter } from 'react-router';
import axios from 'axios';
import { Input, Modal, Button, Form } from 'antd';

import { traderAccountUrl, withdrawFundsUrl, depositFundsUrl } from '../util/constants';
import Navbar from '../component/NavBar';

import 'antd/dist/antd.min.css';
import "./TraderAccount.scss";

import React from 'react';

export default withRouter(class TraderAccountPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            trader: [],
            traderAccount: []
        };
        this.fetchTrader = this.fetchTrader.bind(this);
        this.showDepositModal = this.showDepositModal.bind(this);
        this.showWithdrawModal = this.showWithdrawModal.bind(this);
        this.handleDepositCancel = this.handleDepositCancel.bind(this);
        this.handleWithdrawCancel = this.handleWithdrawCancel.bind(this);
        this.handleDepositOk = this.handleDepositOk.bind(this);
        this.handleWithdrawOk = this.handleWithdrawOk.bind(this);
        this.withdrawalFormRef = React.createRef();
        this.depositFormRef = React.createRef();

    }

    async componentDidMount() {
        if(this.props.match && (this.props.match.params.traderId)) {
            const traderId = this.props.match.params.traderId;
            this.setState({
                traderId
            });
            await this.fetchTrader(traderId);
        }
    }

    async fetchTrader(traderId) {
        const response = await axios.get(traderAccountUrl + traderId);
        if (response) {
            this.setState({
                trader: response.data.trader,
                traderAccount: response.data.account
            });
        }
    }

    showDepositModal() {
        this.setState({
            isDepositModalVisible: true
        });
    }

    showWithdrawModal() {
        // show withdraw modal
        this.setState({
            isWithdrawModalVisible: true
        });
    }

    handleDepositCancel() {
        this.setState({
            isDepositModalVisible: false,
            depositFunds: null
        });
    }

    handleWithdrawCancel() {
        // close withdraw modal & reset withdraw funds
        this.setState({
            isWithdrawModalVisible: false,
            withdrawFunds: null
        });
    }

    async handleDepositOk() {
        const traderDepositUrl = depositFundsUrl + this.state.traderId + "/amount/" + this.state.depositFunds;
        const response = await axios.put(traderDepositUrl);
        if (response) {
            await this.fetchTrader(this.state.traderId);
            this.setState({
                isDepositModalVisible: false
            });
        }
    }

    async handleWithdrawOk() {
        // implement this method
        const traderWithdrawUrl = withdrawFundsUrl + this.state.traderId + "/amount/" + this.state.withdrawFunds;
        const response = await axios.put(traderWithdrawUrl);
        if(response){
            await this.fetchTrader(this.state.traderId);
            this.setState({
                isWithdrawModalVisible: false
            });
        }
    }

    onInputChange(field, value) {
        this.setState({
            [field]: value
        });
    }

    render () {
        return (
            <div className="trader-account-page">
                <Navbar />
                <div className="trader-account-page-content">
                    <div className="title">
                        Trader Account {this.state.traderId}
                    </div>
                    <div className="trader-cards">
                        <div className="trader-card">
                            <div className="info-row">
                                <div className="field">
                                    <div className="content-heading">
                                        First Name
                                    </div>
                                    <div className="content">
                                        { this.state.trader.firstName }
                                    </div>
                                </div>
                                <div className="field">
                                    <div className="content-heading">
                                        Last Name
                                    </div>
                                    <div className="content">
                                        { this.state.trader.lastName }
                                    </div>
                                </div>
                            </div>
                            <div className="info-row">
                                <div className="field">
                                    <div className="content-heading">
                                        Email
                                    </div>
                                    <div className="content">
                                        { this.state.trader.email }
                                    </div>
                                </div>
                            </div>
                            <div className="info-row"> 
                                <div className="field">
                                    <div className="content-heading">
                                        Date of Birth
                                    </div>
                                    <div className="content">
                                        { this.state.trader.dob }
                                    </div>
                                </div>
                                <div className="field">
                                    <div className="content-heading">
                                        Country
                                    </div>
                                    <div className="content">
                                        { this.state.trader.country }
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="trader-card">
                            <div className="info-row">
                                <div className="field">
                                    <div className="content-heading amount">
                                        Amount
                                    </div>
                                    <div className="content amount">
                                        { this.state.traderAccount.amount }$
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="actions">
                            <Button onClick={this.showDepositModal.bind(this)}>Deposit Funds</Button>
                            <Modal 
                                title="Deposit Funds"  
                                okText="Submit" 
                                visible={this.state.isDepositModalVisible} 
                                onOk={() => {
                                    this.depositFormRef.current
                                    .validateFields()
                                    .then((values) => {
                                        this.depositFormRef.current.resetFields();
                                        this.handleDepositOk();
                                    })
                                    .catch((info) => {
                                        console.warn('Validate Failed:', info);
                                    });
                                }}
                                onCancel={this.handleDepositCancel}
                            >
                                <Form
                                    ref={this.depositFormRef}
                                >
                                    <div className="funds-form">
                                        <Form.Item 
                                            name="Amount"
                                            rules={[
                                                {
                                                    required: true,
                                                    pattern: new RegExp(/^[0-9]+$/),
                                                    message:"Amount has to be a whole number greater than 0."
                                                },
                                            ]}
                                        >
                                        <div className="funds-field">
                                            <Input allowClear={false} placeholder="Funds" onChange={(event) => this.onInputChange("depositFunds", event.target.value)} />
                                        </div>
                                        </Form.Item>
                                    </div>
                                </Form>
                            </Modal>
                            
                            <Button onClick={this.showWithdrawModal.bind(this)}>Withdraw Funds</Button>
                            <Modal 
                                title="Withdraw Funds"  
                                okText="Submit" 
                                visible={this.state.isWithdrawModalVisible} 
                                onOk={() => {
                                    this.withdrawalFormRef.current
                                    .validateFields()
                                    .then((values) => {
                                        this.withdrawalFormRef.current.resetFields();
                                        this.handleWithdrawOk();
                                    })
                                    .catch((info) => {
                                        console.warn('Validate Failed:', info);
                                    });
                                }}
                                onCancel={this.handleWithdrawCancel}
                            >
                                <Form
                                    ref={this.withdrawalFormRef}
                                >
                                    <div className="funds-form">
                                        <Form.Item 
                                            name="Amount"
                                            rules={[
                                                {
                                                    required: true,
                                                    pattern: new RegExp(/^[0-9]+$/),
                                                    message:"Amount has to be a whole number greater than 0."
                                                },
                                            ]}
                                        >
                                        <div className="funds-field">
                                            <Input allowClear={false} placeholder="Funds" onChange={(event) => this.onInputChange("withdrawFunds", event.target.value)} />
                                        </div>
                                        </Form.Item>
                                    </div>
                                </Form>
                            </Modal>
                            
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});
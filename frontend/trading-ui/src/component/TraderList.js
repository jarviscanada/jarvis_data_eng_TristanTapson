import { Component } from 'react';
import { Table } from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import 'antd/dist/antd.min.css';
import './TraderList.scss';

export default class TraderList extends Component {

    constructor(props) {
        super(props);

                // Initialization of columns
        const columns = [
            {
                title: 'First Name',
                dataIndex: 'firstName',
                key: 'firstName',
            },
            {
                title: 'Last Name',
                dataIndex: 'lastName',
                key: 'lastName',
            },
            {
                title: 'Email',
                dataIndex: 'email',
                key: 'email',
            },
            {
                title: 'Date of Birth',
                dataIndex: 'dob',
                key: 'dob',
            },
            {
                title: 'Country',
                dataIndex: 'country',
                key: 'country',
            },
            {
                title: 'Actions',
                dataIndex: 'actions',
                key: 'actions'
            },
        ];
        this.state = {
            columns
        }
    }

    componentDidMount() {
                // Mock datasource, since we are not connection to the backend yet
        const dataSource = [
            {
              key: '1',
              id: 1,
              firstName: 'Mike',
              lastName: 'Spencer',
              dob: new Date().toLocaleDateString(),
              country: 'Canada',
              email: 'mike@test.com'
            },
            {
                key: '2',
                id: 2,
                firstName: 'Hellen',
                lastName: 'Miller',
                dob: new Date().toLocaleDateString(),
                country: 'Austria',
                email: 'hellen@test.com'
            },
        ];

        this.setState({
            dataSource
        });
    }
    
        // Render method which returns a Table with defined columns and a mock dataSource
    render() {
        return (
           <Table 
                dataSource={ this.state.dataSource } 
                columns={ this.state.columns } 
                pagination={ false }
            />
        );
    }
}
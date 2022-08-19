import { Component } from 'react';
import { Table } from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faTrashAlt as deleteIcon
} from '@fortawesome/free-solid-svg-icons';
import {
    faSearch as searchIcon
} from '@fortawesome/free-solid-svg-icons';

import { Sorter } from "../util/sorter";

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
                sorter: {
                    compare: Sorter.FIRST
                }
            },
            {
                title: 'Last Name',
                dataIndex: 'lastName',
                key: 'lastName',
                sorter: {
                    compare: Sorter.LAST
                }
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
                sorter: {
                    compare: Sorter.DATE
                }
            },
            {
                title: 'Country',
                dataIndex: 'country',
                key: 'country',
            },
            {   
                // TODO: make the search icon button navigate to the trader/:traderId page 
                title: 'Actions',
                dataIndex: 'actions',
                key: 'actions',
                render: (text, record) => (
                    <div className="icons">
                        <div className="trader-delete-icon">
                            <FontAwesomeIcon icon={ deleteIcon } onClick={() => props.onTraderDeleteClick(record.id) } />
                        </div>
                        <div className="trader-search-icon">
                            <FontAwesomeIcon icon={ searchIcon } onClick={() => props.onTraderShowClick(record.id) } />
                        </div>

                    </div>
                ),
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
                dob: new Date(1995, 6, 14, 12, 30, 0, 0).toLocaleDateString(), // modified to test sorting
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
                dataSource={ this.props.traders } 
                columns={ this.state.columns } 
                pagination={ false }
            />
        );
    }
}
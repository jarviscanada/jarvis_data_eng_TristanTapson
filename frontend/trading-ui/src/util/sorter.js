// sort traders by first name 
const sortFirst = (a,b) => {
	if (a.firstName > b.firstName){
		return 1;
	}
	if (a.firstName < b.firstName){
		return -1;
	}
	return 0;
};

// sort traders by last name
const sortLast = (a,b) => {
	if (a.lastName > b.lastName){
		return 1;
	}
	if (a.lastName < b.lastName){
		return -1;
	}
	return 0;
};

// sort traders by date of birth
const sortDate = (a,b) => {
	if (new Date(a.dob) > new Date(b.dob)){
		return 1;
	}
	if (new Date(a.dob) < new Date (b.dob)){
		return -1;
	}
	return 0;
}

// constructor
export const Sorter = {
	FIRST: sortFirst,
	LAST: sortLast,
	DATE: sortDate
};
// Used to configure the general configuration of some components without modifying the components

import type { SorterResult } from '../components/Table';

export default {
  // basic-table setting
  table: {
    // Form interface request general configuration
    // support xxx.xxx.xxx
    fetchSetting: {
      // The field name of the current page passed to the background
      pageField: 'pageNo',
      // The number field name of each page displayed in the background
      sizeField: 'pageSize',
      // Field name of the form data returned by the interface
      listField: 'list',
      // Total number of tables returned by the interface field name
      totalField: 'count',
    },
    // Number of pages that can be selected
    pageSizeOptions: ['10', '20', '50', '80', '100'],
    // Default display quantity on one page
    defaultPageSize: 20,
    // Custom general sort function
    defaultSortFn: (sortInfo: SorterResult) => {
      const { order, columnKey } = sortInfo;
      if (order && columnKey) {
        return {
          orderBy: columnKey + ' ' + order.replace('end', ''),
        };
      }
    },
    // Custom general filter function
    defaultFilterFn: (data: Partial<Recordable<string[]>>) => {
      return data;
    },
  },
  // scrollbar setting
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    native: false,
  },
};

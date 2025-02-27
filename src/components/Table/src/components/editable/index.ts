import type { BasicColumn } from '../../types/table';
import { useTableContext } from '../../hooks/useTableContext';

import { h, Ref } from 'vue';

import EditableCell from './EditableCell.vue';
import { isObject } from '/@/utils/is';
import { get } from 'lodash-es';

interface Params {
  text: string;
  record: Recordable;
  index: number;
}

export function renderEditCell(column: BasicColumn) {
  return ({ text: value, record, index }: Params) => {
    const table = useTableContext();
    record.onValid = async () => {
      if (isObject(record.editValidCbs)) {
        for (const key in record.editValidCbs) {
          if (!(await record.editValidCbs[key]())) {
            return false;
          }
        }
      }
      return true;
    };

    record.onEdit = async (edit: boolean, submit = false, valid = true) => {
      if (submit) {
        if (record.editable && valid && !(await record.onValid())) {
          return false;
        }
        if (isObject(record.editSubmitCbs)) {
          for (const key in record.editSubmitCbs) {
            await record.editSubmitCbs[key](false, false, edit);
          }
          record.editable = edit;
          !edit && table.emit('edit-row-end');
        }
        return true;
      }
      if (!edit && isObject(record.editCancelCbs)) {
        for (const key in record.editCancelCbs) {
          record.editCancelCbs[key]();
        }
      }
      record.editable = edit;
      return true;
    };

    return h(EditableCell, {
      value,
      labelValue: column.dataLabel && get(record, column.dataLabel),
      tableInstance: table,
      record,
      column,
      index,
    });
  };
}

export type EditRecordRow<T = Recordable> = Partial<
  {
    onValid: () => Promise<boolean>;
    onEdit: (editable: boolean, submit?: boolean) => Promise<boolean>;
    editable: boolean;
    editValidCbs: Fn[];
    editSubmitCbs: Fn[];
    editCancelCbs: Fn[];
    editValueRefs: Recordable<Ref>;
  } & T
>;

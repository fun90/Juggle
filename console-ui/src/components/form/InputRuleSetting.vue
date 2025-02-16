<script lang="ts" setup>
import { ref, watch, PropType } from 'vue';
import {valueType, RuleItem, DataType} from '@/typings';
import { Delete } from '@element-plus/icons-vue';
import {isDataTypeMatch} from '@/utils/dataType';
import FilterValue from '../filter/FilterValue.vue';
import DataTypeDisplay from "@/components/common/DataTypeDisplay.vue";
import VariableSelect from "@/components/common/VariableSelect.vue";

const assignTypeList = [
  { value: valueType.CONSTANT, label: '常量' },
  { value: valueType.VARIABLE, label: '变量' },
];

const props = defineProps({
  modelValue: Array as PropType<RuleItem[]>,
  showRequired: {
    type: Boolean,
    default: false,
  },
  showTargetType: {
    type: Boolean,
    default: true,
  },
  addText: String,
  sourceList: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  targetList: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  requiredKeys: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
});
const emit = defineEmits(['update:modelValue']);
const rules = ref<RuleItem[]>([...(props.modelValue || [])]);

watch(
  () => props.modelValue,
  (val: any) => {
    if (val !== rules.value) {
      rules.value = [...val];
    }
  }
);

const columns = [
  { name: '参数名称', prop: 'source' },
  { name: '数据类型', prop: 'sourceDataType' },
  { name: '赋值方式', prop: 'targetType' },
  { name: '赋值', prop: 'target' },
];

function addRule() {
  rules.value.push({
    source: '',
    sourceDataType: null,
    sourceType: valueType.VARIABLE,
    target: '',
    targetDataType: null,
    targetType: valueType.INPUT_PARAM,
    required: false,
  });
  onChange();
}

function removeRule(rowIndex: number) {
  rules.value.splice(rowIndex, 1);
  onChange();
}

function onChange() {
  emit('update:modelValue', rules.value);
}

function onSourceTypeChange(rowIndex: number) {
  rules.value[rowIndex].source = '';
  rules.value[rowIndex].sourceDataType = null;
  onChange();
}

function onSourceVarChange(rowIndex: number) {
  const source = rules.value[rowIndex].source;
  const param = props.sourceList!.find(item => item.envKey === source);
  rules.value[rowIndex].sourceDataType = param?.dataType;
  onChange();
}

function getAvailableTarget(target: string) {
  return props.sourceList!.filter(item => {
    // 已选参数也能选
    if (item.paramKey === target) {
      return item;
    }
    // 只能选未被选择的参数
    return !rules.value.map(item => item.target).includes(item.paramKey);
  });
}
function getAvailableSource(target: string, targetDataType: DataType) {
  //console.log(props.sourceList,target,targetDataType)
  return props.targetList!.filter(item => {
    // 不选取自己
    if (item.envKey === target) {
      return false;
    }
    // 只能选与自己类型一致的
    return isDataTypeMatch(item.dataType, targetDataType);
  });
}

function onTargetChange(rowIndex: number) {
  console.log('onTargetChange', rules.value[rowIndex]);
  const target = rules.value[rowIndex].target;
  const param = props.sourceList!.find(item => item.paramKey === target);
  rules.value[rowIndex].source = '';
  rules.value[rowIndex].targetDataType = param.dataType;
  rules.value[rowIndex].targetType = param.targetType;
  if (props.requiredKeys!.includes(target)) {
    rules.value[rowIndex].required = true;
  }
}
</script>

<template>
  <div class="rule-setting">
    <div class="rule-setting-head">
      <div class="rule-setting-tr">
        <template v-for="column in columns" :key="column.prop">
          <template v-if="column.prop === 'targetType'">
            <div class="rule-setting-td" v-if="showTargetType">{{ column.name }}</div>
          </template>
          <div class="rule-setting-td" v-else>{{ column.name }}</div>
        </template>
        <div class="rule-setting-td delete-td"></div>
      </div>
    </div>
    <div class="rule-setting-body">
      <div class="rule-setting-tr" v-for="(rule, rowIndex) in rules" :key="rowIndex">
        <template v-for="column in columns" :key="column.prop">
          <div class="rule-setting-td" v-if="column.prop === 'source'">
            <div class="required"><span v-if="requiredKeys.includes(rule.target)">*</span></div>
            <el-select v-model="rule.target" :disabled="requiredKeys.includes(rule.target)" size="small" @change="onTargetChange(rowIndex)">
              <el-option
                v-for="item in getAvailableTarget(rule.target)"
                :key="item.paramKey"
                :value="item.paramKey"
                :label="item.paramName"
                :title="item.paramName"
              />
            </el-select>
          </div>
          <div class="rule-setting-td" v-if="column.prop === 'sourceDataType'">
            <DataTypeDisplay :dataType="rule.targetDataType as DataType"/>
          </div>
          <div class="rule-setting-td" v-if="column.prop === 'targetType' && showTargetType">
            <el-select v-model="rule.sourceType" size="small" @change="onSourceTypeChange(rowIndex)">
              <el-option v-for="item in assignTypeList" :key="item.value" :value="item.value" :label="item.label" />
            </el-select>
          </div>
          <div class="rule-setting-td" v-if="column.prop === 'target'">
            <!-- 常量 -->
            <template v-if="rule.sourceType === valueType.CONSTANT">
              <FilterValue v-model="rule.source" :dataType="rule.targetDataType" size="small" :showNumberControls="false" />
            </template>
            <!-- 变量 -->
            <VariableSelect
                v-else
                v-model="rule.source"
                size="small"
                :options="getAvailableSource(rule.target, rule.targetDataType as DataType)"
                :filterDataType="rule.targetDataType as DataType"
                @change="onSourceVarChange(rowIndex)"
            />
          </div>
        </template>
        <div class="rule-setting-td delete-td">
          <el-icon @click="removeRule(rowIndex)" v-if="!requiredKeys.includes(rule.target)"><Delete /></el-icon>
        </div>
      </div>
    </div>
    <div class="rule-setting-foot">
      <el-button size="small" type="info" @click="addRule">{{ addText || '新增入参' }}</el-button>
    </div>
  </div>
</template>

<style lang="less" scoped>
.rule-setting {
  width: 100%;
  &-head {
    background-color: #f2f2f2;
    padding: 0 1px;
  }
  &-body {
    border-left: 1px solid #f2f2f2;
    border-right: 1px solid #f2f2f2;
  }
  &-tr {
    display: flex;
    border-bottom: 1px solid #f2f2f2;
    height: 36px;
  }
  &-td {
    flex: 1;
    min-width: 0;
    padding: 0 6px;
    display: flex;
    align-items: center;
    .required {
      color: red;
      width: 10px;
    }
    &.delete-td {
      width: 40px;
      flex: none;
      justify-content: center;
    }
    &.delete-td {
      width: 20px;
      margin-right: 10px;
      & > .el-icon {
        cursor: pointer;
        color: #999;
      }
    }
  }
  &-foot {
    text-align: center;
    padding: 6px 0;
  }
}

.rule-setting-td{
  .dataTypeName {
    color: var(--el-text-color-regular);
  }
}
</style>

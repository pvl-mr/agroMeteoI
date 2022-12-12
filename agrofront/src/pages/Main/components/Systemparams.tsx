import { FC, useState } from 'react'
import dayjs from 'dayjs'
import 'dayjs/locale/ru'

import DatetimePicker from 'components/DatetimePicker'

import { Period, MeteoBlock } from '../styles'
import { METEO_PARAM_KEYS } from 'сonstants/static'
import ItemMeteoParam from './ItemMeteoParam'

dayjs.locale('ru')
// eslint-disable-next-line @typescript-eslint/ban-types
type Props = {}

enum DATE_FILTER {
  START = 'start',
  END = 'end',
}

const timestampToString = (timestamp: number | null): string | null => {
  if (!timestamp) {
    return null
  }

  return dayjs.unix(timestamp).format('YYYY-MM-DDTHH:mm')
}

const Systemparams: FC<Props> = (): JSX.Element => {
  const [startTime, setStartTime] = useState<number>(
    dayjs().subtract(1, 'month').unix()
  )
  const [endTime, setEndTime] = useState<number>(dayjs().unix())

  const handleChangeDate = (e: any): void => {
    const { value, name } = e.target
    const timestamp = dayjs(value).unix()
    if (name === DATE_FILTER.START) {
      setStartTime(timestamp)
      return
    }
    setEndTime(timestamp)
  }

  return (
    <MeteoBlock>
      <Period>
        <DatetimePicker
          label="От"
          value={timestampToString(startTime)}
          onChange={handleChangeDate}
          name={DATE_FILTER.START}
        />
        <DatetimePicker
          label="До"
          value={timestampToString(endTime)}
          onChange={handleChangeDate}
          name={DATE_FILTER.END}
        />
      </Period>
      <ItemMeteoParam
        startTime={startTime}
        endTime={endTime}
        parameterName={METEO_PARAM_KEYS.BATTERY}
        label="Батарея"
      />
    </MeteoBlock>
  )
}

export default Systemparams

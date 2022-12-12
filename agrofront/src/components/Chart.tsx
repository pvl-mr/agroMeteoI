import { useRef } from 'react'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  BarElement,
} from 'chart.js'
import { Line, Bar } from 'react-chartjs-2'
import { useReactToPrint } from 'react-to-print'
import { Button } from '@mui/material'
import styled from 'styled-components'

import { METEO_PARAM_KEYS } from 'сonstants/static'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  BarElement
)

export const options = {
  responsive: true,
  interaction: {
    mode: 'index' as const,
    intersect: false,
  },
  stacked: false,
  plugins: {
    title: {
      display: false,
      text: '',
    },
  },
  scales: {
    y: {
      type: 'linear' as const,
      display: true,
      position: 'left' as const,
    },
    y1: {
      type: 'linear' as const,
      display: true,
      position: 'right' as const,
      grid: {
        drawOnChartArea: false,
      },
    },
  },
}

export const barOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    title: {
      display: false,
      text: '',
    },
  },
}

type DatasetType = {
  label: string
  data: string[] | number[]
}

type Props = {
  labels: string[]
  datasets: DatasetType[]
  parameterName: string
}

const WrapperButton = styled.div`
  display: flex;
`

export default function Chart({
  labels,
  datasets,
  parameterName,
}: Props): JSX.Element {
  const data = {
    labels,
    datasets: datasets.map((dataset) => ({
      borderColor: 'rgb(53, 162, 235)',
      backgroundColor: 'rgba(53, 162, 235, 0.5)',
      yAxisID: 'y1',
      ...dataset,
    })),
  }
  const chartRef = useRef(null)
  const handlePrint = useReactToPrint({
    content: () => chartRef.current,
  })

  return (
    <div>
      <WrapperButton>
        <Button onClick={handlePrint}>Печать</Button>
      </WrapperButton>
      <div ref={chartRef}>
        <Line options={options} data={data} />
      </div>
    </div>
  )
}

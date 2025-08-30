package com.example.bhuwicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhuwicalc.ui.theme.BhuwicalcTheme
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BhuwicalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var input by remember { mutableStateOf("0") }
    var canAddOperation by remember { mutableStateOf(false) }
    var canAddDecimal by remember { mutableStateOf(true) }

    fun appendInput(value: String) {
        if (input == "0" && value != ".") {
            input = value
        } else {
            input += value
        }
        canAddOperation = value != "."
        if (value == ".") canAddDecimal = false
    }

    fun clearInput() {
        input = "0"
        canAddOperation = false
        canAddDecimal = true
    }

    fun calculate(): String {
        return try {
            val cleanedExpression = input.replace("×", "*").replace("÷", "/").replace("−", "-")
            val result = ExpressionBuilder(cleanedExpression).build().evaluate()
            if (result % 1 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = input,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray)
                .padding(16.dp),
            maxLines = 2,
            softWrap = true
        )

        val buttonModifier = Modifier
            .weight(1f)
            .aspectRatio(1f)

        val buttonColors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)

        // Row 1: Clear, Divide, Multiply
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                modifier = buttonModifier.weight(2f),
                colors = buttonColors,
                onClick = { clearInput() },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("C", fontSize = 24.sp)
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = {
                    if (canAddOperation) {
                        appendInput("÷")
                        canAddOperation = false
                        canAddDecimal = true
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("÷", fontSize = 24.sp)
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = {
                    if (canAddOperation) {
                        appendInput("×")
                        canAddOperation = false
                        canAddDecimal = true
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("×", fontSize = 24.sp)
            }
        }

        // Row 2: 7,8,9, -
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("7", "8", "9").forEach { digit ->
                Button(
                    modifier = buttonModifier,
                    colors = buttonColors,
                    onClick = { appendInput(digit) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(digit, fontSize = 24.sp)
                }
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = {
                    if (canAddOperation) {
                        appendInput("−")
                        canAddOperation = false
                        canAddDecimal = true
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("−", fontSize = 24.sp)
            }
        }

        // Row 3: 4,5,6, +
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("4", "5", "6").forEach { digit ->
                Button(
                    modifier = buttonModifier,
                    colors = buttonColors,
                    onClick = { appendInput(digit) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(digit, fontSize = 24.sp)
                }
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = {
                    if (canAddOperation) {
                        appendInput("+")
                        canAddOperation = false
                        canAddDecimal = true
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
        }

        // Row 4: 1,2,3, =
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("1", "2", "3").forEach { digit ->
                Button(
                    modifier = buttonModifier,
                    colors = buttonColors,
                    onClick = { appendInput(digit) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(digit, fontSize = 24.sp)
                }
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = { input = calculate() },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("=", fontSize = 24.sp)
            }
        }

        // Row 5: 0, .
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                modifier = buttonModifier.weight(2f),
                colors = buttonColors,
                onClick = { appendInput("0") },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("0", fontSize = 24.sp)
            }
            Button(
                modifier = buttonModifier,
                colors = buttonColors,
                onClick = {
                    if (canAddDecimal) {
                        appendInput(".")
                        canAddDecimal = false
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(".", fontSize = 24.sp)
            }
        }
    }
}

package com.example.bhuwicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                    color = Color(0xFF1C1C1E)
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
                String.format("%.8f", result).trimEnd('0').trimEnd('.')
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1C1C1E),
                        Color(0xFF2C2C2E)
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Display Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2C2C2E),
                            Color(0xFF3A3A3C)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = input,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.End,
                maxLines = 2,
                lineHeight = 50.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1: Clear, +/-, %, ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CalculatorButton(
                    text = "AC",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFA6A6A6),
                    textColor = Color.Black,
                    onClick = { clearInput() }
                )
                CalculatorButton(
                    text = "±",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFA6A6A6),
                    textColor = Color.Black,
                    onClick = {
                        if (input != "0" && !input.startsWith("-")) {
                            input = "-$input"
                        } else if (input.startsWith("-")) {
                            input = input.substring(1)
                        }
                    }
                )
                CalculatorButton(
                    text = "%",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFA6A6A6),
                    textColor = Color.Black,
                    onClick = {
                        if (canAddOperation) {
                            val result = calculate()
                            if (result != "Error") {
                                val percentage = result.toDouble() / 100
                                input = if (percentage % 1 == 0.0) {
                                    percentage.toInt().toString()
                                } else {
                                    percentage.toString()
                                }
                            }
                        }
                    }
                )
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9F0A),
                    textColor = Color.White,
                    onClick = {
                        if (canAddOperation) {
                            appendInput("÷")
                            canAddOperation = false
                            canAddDecimal = true
                        }
                    }
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("7", "8", "9").forEach { digit ->
                    CalculatorButton(
                        text = digit,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        textColor = Color.White,
                        onClick = { appendInput(digit) }
                    )
                }
                CalculatorButton(
                    text = "×",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9F0A),
                    textColor = Color.White,
                    onClick = {
                        if (canAddOperation) {
                            appendInput("×")
                            canAddOperation = false
                            canAddDecimal = true
                        }
                    }
                )
            }

            // Row 3: 4, 5, 6, −
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("4", "5", "6").forEach { digit ->
                    CalculatorButton(
                        text = digit,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        textColor = Color.White,
                        onClick = { appendInput(digit) }
                    )
                }
                CalculatorButton(
                    text = "−",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9F0A),
                    textColor = Color.White,
                    onClick = {
                        if (canAddOperation) {
                            appendInput("−")
                            canAddOperation = false
                            canAddDecimal = true
                        }
                    }
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("1", "2", "3").forEach { digit ->
                    CalculatorButton(
                        text = digit,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFF505050),
                        textColor = Color.White,
                        onClick = { appendInput(digit) }
                    )
                }
                CalculatorButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9F0A),
                    textColor = Color.White,
                    onClick = {
                        if (canAddOperation) {
                            appendInput("+")
                            canAddOperation = false
                            canAddDecimal = true
                        }
                    }
                )
            }

            // Row 5: 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(2f),
                    backgroundColor = Color(0xFF505050),
                    textColor = Color.White,
                    onClick = { appendInput("0") },
                    isWide = true
                )
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF505050),
                    textColor = Color.White,
                    onClick = {
                        if (canAddDecimal) {
                            appendInput(".")
                            canAddDecimal = false
                        }
                    }
                )
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFFFF9F0A),
                    textColor = Color.White,
                    onClick = {
                        val result = calculate()
                        input = result
                        canAddOperation = result != "Error"
                        canAddDecimal = !result.contains(".")
                    }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    isWide: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(80.dp)
            .then(
                if (isWide) Modifier.fillMaxWidth() else Modifier.aspectRatio(1f)
            ),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = if (isWide) RoundedCornerShape(40.dp) else CircleShape,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}